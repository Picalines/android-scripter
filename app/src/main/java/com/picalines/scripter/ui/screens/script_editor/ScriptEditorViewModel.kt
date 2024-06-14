package com.picalines.scripter.ui.screens.script_editor

import android.util.Log
import com.ooooonly.luakt.toTypedArray
import com.picalines.scripter.LOGIN_SCREEN
import com.picalines.scripter.SCRIPT_DEFAULT_ID
import com.picalines.scripter.domain.Script
import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.domain.service.StorageService
import com.picalines.scripter.ui.screens.ScripterAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.jse.JsePlatform
import javax.inject.Inject

@HiltViewModel
class ScriptEditorViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
) : ScripterAppViewModel() {
    val script = MutableStateFlow(DEFAULT_SCRIPT)
    val scriptOutput = MutableStateFlow("")
    val running = MutableStateFlow(false)

    val requestingInput = MutableStateFlow(false)
    val inputPrompt = MutableStateFlow("")
    val currentInput = MutableStateFlow("")

    private var saveJob: Job? = null

    fun initialize(scriptId: String, restartApp: (String) -> Unit) {
        launchCatching {
            script.value = storageService.readScript(scriptId) ?: DEFAULT_SCRIPT
        }

        observeAuthenticationState(restartApp)
    }

    private fun observeAuthenticationState(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(LOGIN_SCREEN)
            }
        }
    }

    fun updateScript(newCode: String) {
        script.value = script.value.copy(sourceCode = newCode)

        saveJob?.cancel()
        saveJob = launchCatching {
            delay(500)

            if (script.value.id == SCRIPT_DEFAULT_ID) {
                storageService.createScript(script.value)
                    ?.let { newId ->
                        script.value = script.value.copy(id = newId)
                    }
            } else {
                storageService.updateScript(script.value)
            }
        }
    }

    fun updateInput(input: String) {
        currentInput.value = input
    }

    fun submitInput() {
        requestingInput.value = false
    }

    fun executeScript() {
        scriptOutput.value = ""

        val code = script.value.sourceCode

        val globals = JsePlatform.standardGlobals()

        val luaPrint = LambdaLuaFunction.void { args ->
            val message = args.toTypedArray().joinToString(" ") { it.tostring().toString() }
            val history = scriptOutput.value
            scriptOutput.value = (if (history.isNotEmpty()) history + "\n" else "") + message
        }
        globals.set("print", luaPrint)

        globals.set("delay", LambdaLuaFunction.voidAsync { args ->
            delay(args.checknumber(1).toint().toLong())
        })

        globals.set("input", LambdaLuaFunction.async { args ->
            inputPrompt.value = args.toTypedArray().joinToString(" ") { it.tostring().toString() }
            currentInput.value = ""
            requestingInput.value = true
            while (requestingInput.value) {
                delay(10)
            }
            delay(500)
            luaPrint.invoke(LuaValue.valueOf("${inputPrompt.value}: ${currentInput.value}"))
            LuaValue.valueOf(currentInput.value)
        })

        val luaExecutable = globals.load(code)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                running.value = true
                luaExecutable.invoke()
            } catch (e: LuaError) {
                val errorMessage = (e.message ?: "\n ").lines().last().split(" ", limit = 2)[1]
                luaPrint.invoke(LuaString.valueOf("ERROR: $errorMessage"))
            } finally {
                running.value = false
            }
        }
    }

    companion object {
        private val DEFAULT_SCRIPT = Script(id = SCRIPT_DEFAULT_ID)
    }

    private class LambdaLuaFunction(val lambda: (Varargs) -> Varargs) : VarArgFunction() {
        override fun invoke(varargs: Varargs) = lambda(varargs)

        companion object {
            fun void(lambda: (Varargs) -> Unit) =
                LambdaLuaFunction { args -> lambda(args); LuaValue.NIL }

            fun voidAsync(lambda: suspend (Varargs) -> Unit) = LambdaLuaFunction { args ->
                runBlocking { lambda(args) }
                LuaValue.NIL
            }

            fun async(lambda: suspend (Varargs) -> LuaValue) = LambdaLuaFunction { args ->
                var result: LuaValue
                runBlocking { result = lambda(args) }
                result
            }
        }
    }
}
