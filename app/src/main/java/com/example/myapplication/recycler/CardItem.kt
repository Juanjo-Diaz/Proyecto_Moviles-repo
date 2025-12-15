package com.example.myapplication.recycler

data class CardItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageResId: Int,
    val isFavorite: Boolean = false
)
