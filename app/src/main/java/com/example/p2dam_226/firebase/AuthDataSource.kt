package com.example.p2dam_226.firebase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await

class AuthDataSource(private val auth: com.google.firebase.auth.FirebaseAuth) {
    suspend fun signIn(email: String, pass: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            result.user?.let { Result.success(it) } ?: Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, pass: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            result.user?.let { Result.success(it) } ?: Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getAuthStateFlow(): kotlinx.coroutines.flow.Flow<FirebaseUser?> = kotlinx.coroutines.flow.callbackFlow {
        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { 
            trySend(it.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }
}
