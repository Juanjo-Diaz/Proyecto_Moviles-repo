package com.example.p2dam_226.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p2dam_226.firebase.AuthRepository
import com.example.p2dam_226.firebase.BookRepository
import com.example.p2dam_226.firebase.Book
import com.example.p2dam_226.recycler.CardItem
import com.example.p2dam_226.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ListViewModel(
    private val bookRepository: BookRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _ascending = MutableStateFlow(true)

    private val _books = authRepository.getAuthStateFlow().flatMapLatest { user ->
        if (user != null) {
            bookRepository.getBooks(user.uid)
        } else {
            flowOf(emptyList())
        }
    }

    val items: StateFlow<List<CardItem>> = combine(_books, _query, _ascending) { books, query, asc ->
        val filtered = if (query.isBlank()) books else books.filter {
            it.titulo.contains(query, ignoreCase = true)
        }
        val sorted = filtered.sortedWith(compareBy<Book> { it.titulo.lowercase() }
            .let { cmp -> if (asc) cmp else cmp.reversed() })
            
        sorted.map { book ->
            CardItem(
                id = book.id,
                title = book.titulo,
                description = book.desc,
                imageResId = getImageForBook(book.titulo),
                isFavorite = book.fav
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<CardItem>> = items.map { list ->
        list.filter { it.isFavorite }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(text: String) { _query.value = text }
    fun toggleSort() { _ascending.value = !_ascending.value }

    fun toggleFavorite(bookId: String) {
        val user = authRepository.getCurrentUser() ?: return
        val item = items.value.find { it.id == bookId } ?: return
        
        viewModelScope.launch {
            bookRepository.updateFavorite(user.uid, bookId, !item.isFavorite)
        }
    }

    fun addBook(title: String, desc: String, fav: Boolean) {
        val user = authRepository.getCurrentUser() ?: return
        val book = Book(titulo = title, desc = desc, fav = fav)
        viewModelScope.launch {
            bookRepository.addBook(user.uid, book)
        }
    }

    private fun getImageForBook(title: String): Int {
        val images = listOf(
            R.drawable.ic_mtg_bolt,
            R.drawable.ic_mtg_counterspell,
            R.drawable.ic_mtg_llanowar,
            R.drawable.ic_mtg_shivan
        )
        return images[abs(title.hashCode()) % images.size]
    }
}
