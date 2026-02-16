package com.example.p2dam_226.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BookDataSource(private val firestore: FirebaseFirestore) {

    fun getBooks(userId: String): Flow<List<Book>> = callbackFlow {
        val collection = firestore.collection("usuarios").document(userId).collection("libros")
        val subscription = collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val books = snapshot.toObjects(Book::class.java)
                trySend(books)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun addBook(userId: String, book: Book): Result<Unit> {
        return try {
            val collection = firestore.collection("usuarios").document(userId).collection("libros")
            if (book.id.isNotEmpty()) {
                collection.document(book.id).set(book).await()
            } else {
                collection.add(book).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFavorite(userId: String, bookId: String, isFav: Boolean): Result<Unit> {
        return try {
            firestore.collection("usuarios").document(userId).collection("libros")
                .document(bookId).set(mapOf("fav" to isFav), com.google.firebase.firestore.SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
