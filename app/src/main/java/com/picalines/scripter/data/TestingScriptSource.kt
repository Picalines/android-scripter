package com.picalines.scripter.data

import android.os.Build
import com.google.firebase.Timestamp
import com.picalines.scripter.domain.Script
import com.picalines.scripter.domain.ScriptLanguage
import java.time.LocalDateTime

class TestingScriptSource {
    fun getScripts(): List<Script> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            listOf(
                Script(
                    id = "1",
                    userId = "1-test",
                    language = ScriptLanguage.Lua,
                    name = "Lua hello world",
                    sourceCode = "print(\"hello world!\")",
                    tags = listOf(),
                    createdAt = Timestamp.now()
                ), Script(
                    id = "2",
                    userId = "1-test",
                    language = ScriptLanguage.Lua,
                    name = "Android toast",
                    sourceCode = "showToast(\"hi from Lua!\")",
                    tags = listOf(),
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now()
                )
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}