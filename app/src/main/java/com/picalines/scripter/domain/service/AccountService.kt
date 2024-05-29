package com.picalines.scripter.domain.service

import com.picalines.scripter.domain.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String?

    fun hasUser(): Boolean

    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
}