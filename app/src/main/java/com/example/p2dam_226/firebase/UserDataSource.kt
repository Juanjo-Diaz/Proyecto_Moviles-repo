package com.example.p2dam_226.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDataSource(private val firestore: FirebaseFirestore) {
    suspend fun createUser(userId: String, email: String, birthdate: String): Result<Unit> {
        return try {
            val user = hashMapOf(
                "correo" to email,
                "fechaNacimiento" to birthdate
            )
            firestore.collection("usuarios").document(userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
