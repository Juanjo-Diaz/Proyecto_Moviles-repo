package com.example.p2dam_226.firebase

import com.google.firebase.auth.FirebaseUser

class AuthRepository(private val dataSource: AuthDataSource) {
    suspend fun signIn(email: String, pass: String): Result<FirebaseUser> = dataSource.signIn(email, pass)
    
    suspend fun signUp(email: String, pass: String): Result<FirebaseUser> = dataSource.signUp(email, pass)
    
    fun signOut() = dataSource.signOut()
    
    fun getCurrentUser(): FirebaseUser? = dataSource.getCurrentUser()
    
    fun getAuthStateFlow() = dataSource.getAuthStateFlow()
}
