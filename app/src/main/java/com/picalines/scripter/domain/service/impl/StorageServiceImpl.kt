package com.picalines.scripter.domain.service.impl

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.picalines.scripter.domain.Script
import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.domain.service.StorageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import java.util.UUID
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

    override suspend fun createScript(script: Script): String? {
        val scriptToAdd =
            script.copy(
                userId = auth.currentUserId ?: return null,
                id = UUID.randomUUID().toString(),
                name = getScriptName(script),
                createdAt = Timestamp.now()
            )

        Firebase.firestore.collection(SCRIPTS_COLLECTION)
            .document(scriptToAdd.id)
            .set(scriptToAdd).await()

        return scriptToAdd.id
    }

    override suspend fun readScript(scriptId: String): Script? {
        return Firebase.firestore.collection(SCRIPTS_COLLECTION).document(scriptId).get().await()
            .toObject(Script::class.java)
    }

    override suspend fun updateScript(script: Script) {
        Firebase.firestore
            .collection(SCRIPTS_COLLECTION)
            .document(script.id)
            .set(
                script.copy(name = getScriptName(script), updatedAt = Timestamp.now())
            )
            .await()
    }

    override suspend fun deleteScript(scriptId: String) {
        Firebase.firestore.collection(SCRIPTS_COLLECTION).document(scriptId).delete().await()
    }

    private fun getScriptName(script: Script): String {
        val nameRegex = Regex("""^--\s*(.+)$""", RegexOption.MULTILINE)
        return nameRegex.find(script.sourceCode)?.groups?.get(1)?.value ?: "Untitled"
    }
}