package com.example.p2dam_226.firebase

class UserRepository(private val dataSource: UserDataSource) {
    suspend fun createUser(userId: String, email: String, birthdate: String) = dataSource.createUser(userId, email, birthdate)
}
