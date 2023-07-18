package com.tunahan.ecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.tunahan.ecommerceapp.room.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context,FavoriteDatabase::class.java,"favoriteDB").build()

    @Singleton
    @Provides
    fun injectDao(database:FavoriteDatabase)=database.favoriteDao()
}