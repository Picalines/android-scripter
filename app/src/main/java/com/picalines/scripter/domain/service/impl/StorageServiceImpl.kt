package com.picalines.scripter.domain.service.impl

import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.picalines.scripter.domain.Script
import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.domain.service.StorageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val SCRIPTS_COLLECTION = "scripts"
const val USER_ID_FIELD = "userId"

class StorageServiceImpl @Inject constructor(private val auth: AccountService) : StorageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val scripts: Flow<List<Script>>
        get() = auth.currentUser.flatMapLatest { user ->
            Firebase.firestore.collection(SCRIPTS_COLLECTION).whereEqualTo(USER_ID_FIELD, user?.id)
                .dataObjects()
        }

    override suspend fun createScript(script: Script) {
        val scriptWithUserId = script.copy(userId = auth.currentUserId ?: return)
        Firebase.firestore.collection(SCRIPTS_COLLECTION).add(scriptWithUserId).await()
    }

    override suspend fun readScript(scriptId: String): Script? {
        return Firebase.firestore.collection(SCRIPTS_COLLECTION).document(scriptId).get().await()
            .toObject(Script::class.java)
    }

    override suspend fun updateScript(script: Script) {
        Firebase.firestore.collection(SCRIPTS_COLLECTION).document(script.id).set(script).await()
    }

    override suspend fun deleteScript(scriptId: String) {
        Firebase.firestore.collection(SCRIPTS_COLLECTION).document(scriptId).delete().await()
    }
}