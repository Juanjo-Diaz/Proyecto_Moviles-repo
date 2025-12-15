package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.recycler.CardItem

class ListViewModel : ViewModel() {
    private val baseItems = MutableLiveData<List<CardItem>>(emptyList())
    private val query = MutableLiveData("")
    private val ascending = MutableLiveData(true)

    private val _items = MediatorLiveData<List<CardItem>>().apply {
        fun recompute() {
            val src = baseItems.value.orEmpty()
            val filtered = if (query.value.isNullOrBlank()) src else src.filter {
                it.title.contains(query.value!!, ignoreCase = true)
            }
            val sorted = filtered.sortedWith(compareBy<CardItem> { it.title.lowercase() }
                .let { cmp -> if (ascending.value == true) cmp else cmp.reversed() })
            value = sorted
        }
        addSource(baseItems) { recompute() }
        addSource(query) { recompute() }
        addSource(ascending) { recompute() }
    }
    val items: LiveData<List<CardItem>> = _items

    private val _favorites = MediatorLiveData<List<CardItem>>().apply {
        addSource(_items) { list ->
            value = list.filter { it.isFavorite }
        }
    }
    val favorites: LiveData<List<CardItem>> = _favorites

    fun setItems(list: List<CardItem>) {
        if (baseItems.value.isNullOrEmpty()) {
            baseItems.value = list
        }
    }

    fun toggleFavorite(id: Int) {
        val current = baseItems.value ?: return
        baseItems.value = current.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }

    fun setQuery(text: String) { query.value = text }
    fun toggleSort() { ascending.value = !(ascending.value ?: true) }
}
