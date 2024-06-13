package com.picalines.scripter.domain.service

import com.picalines.scripter.domain.Script
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val scripts: Flow<List<Script>>

    suspend fun createScript(script: Script)
    suspend fun readScript(scriptId: String): Script?
    suspend fun updateScript(script: Script)
    suspend fun deleteScript(scriptId: String)
}