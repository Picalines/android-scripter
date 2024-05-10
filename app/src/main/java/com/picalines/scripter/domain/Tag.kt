package com.picalines.scripter.domain

import androidx.compose.ui.graphics.Color

data class Tag(
    val name: String,
    val color: Color
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "color" to color,
        )
    }
}

