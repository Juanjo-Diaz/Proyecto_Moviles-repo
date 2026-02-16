package com.example.p2dam_226.firebase

object ServiceLocator {
    val authDataSource by lazy { AuthDataSource(FirebaseProvider.provideAuth()) }
    val authRepository by lazy { AuthRepository(authDataSource) }

    val userDataSource by lazy { UserDataSource(FirebaseProvider.provideFirestore()) }
    val userRepository by lazy { UserRepository(userDataSource) }

    val bookDataSource by lazy { BookDataSource(FirebaseProvider.provideFirestore()) }
    val bookRepository by lazy { BookRepository(bookDataSource) }
}
