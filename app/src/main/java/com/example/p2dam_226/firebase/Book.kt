package com.example.p2dam_226.firebase

import com.google.firebase.firestore.DocumentId

data class Book(
    @DocumentId val id: String = "",
    val titulo: String = "",
    val desc: String = "",
    val fav: Boolean = false
)
