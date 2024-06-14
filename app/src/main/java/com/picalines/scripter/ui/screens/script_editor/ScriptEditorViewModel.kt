package com.picalines.scripter.ui.screens.script_editor

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.picalines.scripter.LOGIN_SCREEN
import com.picalines.scripter.SCRIPT_DEFAULT_ID
import com.picalines.scripter.domain.Script
import com.picalines.scripter.domain.ScriptLanguage
import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.domain.service.StorageService
import com.picalines.scripter.ui.screens.ScripterAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class ScriptEditorViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
) : ScripterAppViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    val script = MutableStateFlow(DEFAULT_SCRIPT)

    private var saveJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    fun executeScript() {

    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val DEFAULT_SCRIPT = Script(id = SCRIPT_DEFAULT_ID)
    }
}
