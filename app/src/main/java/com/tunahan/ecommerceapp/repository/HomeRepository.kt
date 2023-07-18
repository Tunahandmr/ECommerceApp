package com.tunahan.ecommerceapp.repository

import androidx.lifecycle.LiveData
import com.tunahan.ecommerceapp.model.Document
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.room.FavoriteDao
import javax.inject.Inject

class HomeRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    val readAllData: LiveData<List<Favorite>> = favoriteDao.readAllData()

    suspend fun addNote(favorite: Favorite) {
        favoriteDao.addFavorite(favorite)
    }

    suspend fun updateDocument(favorite: Favorite){
        favoriteDao.updateFavorite(favorite)
    }

    suspend fun deleteNote(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }

}