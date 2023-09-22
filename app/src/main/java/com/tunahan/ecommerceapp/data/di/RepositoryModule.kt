package com.tunahan.ecommerceapp.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tunahan.ecommerceapp.data.local.dao.CartDao
import com.tunahan.ecommerceapp.data.local.dao.FavoriteDao
import com.tunahan.ecommerceapp.data.repository.FirebaseRepositoryImpl
import com.tunahan.ecommerceapp.data.repository.LocalDatabaseRepositoryImpl
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun injectLocalDBRepo(favoriteDao: FavoriteDao, cartDao: CartDao): LocalDatabaseRepository =
        LocalDatabaseRepositoryImpl(favoriteDao, cartDao)

    @Singleton
    @Provides
    fun injectFirebaseRepo(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): FirebaseRepository = FirebaseRepositoryImpl(auth, firestore, storage)
}