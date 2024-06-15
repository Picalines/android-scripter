package com.picalines.scripter.domain

import com.google.firebase.Timestamp

data class Script(
    val id: String = "",
    val userId: String = "",
    val language: ScriptLanguage = ScriptLanguage.Lua,
    val name: String = "untitled",
    val sourceCode: String = "",
    val tags: List<Tag> = emptyList(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp? = null,
    val ranAt: Timestamp? = null,
    val ranTimes: Int = 0,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "language" to language,
            "name" to name,
            "sourceCode" to sourceCode,
            "tags" to tags,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt,
            "ranAt" to ranAt,
            "ranTimes" to ranTimes,
        )
    }
}

