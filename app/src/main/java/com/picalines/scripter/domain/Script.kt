package com.picalines.scripter.domain

import java.time.LocalDateTime

data class Script(
    val id: String,
    val userId: String,
    val language: ScriptLanguage,
    val name: String,
    val sourceCode: String,
    val tags: List<Tag>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
    val ranAt: LocalDateTime? = null,
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

