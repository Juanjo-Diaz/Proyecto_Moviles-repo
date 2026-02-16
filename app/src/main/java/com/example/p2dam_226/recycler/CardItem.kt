package com.example.p2dam_226.recycler

data class CardItem(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val isFavorite: Boolean = false
)
