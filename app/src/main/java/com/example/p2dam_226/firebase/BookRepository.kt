package com.example.p2dam_226.firebase

import kotlinx.coroutines.flow.Flow

class BookRepository(private val dataSource: BookDataSource) {
    fun getBooks(userId: String): Flow<List<Book>> = dataSource.getBooks(userId)
    suspend fun addBook(userId: String, book: Book) = dataSource.addBook(userId, book)
    suspend fun updateFavorite(userId: String, bookId: String, isFav: Boolean) = dataSource.updateFavorite(userId, bookId, isFav)
}
