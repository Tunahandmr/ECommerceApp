package com.tunahan.ecommerceapp.repository

import androidx.lifecycle.LiveData
import com.tunahan.ecommerceapp.model.Cart
import com.tunahan.ecommerceapp.model.Document
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.room.CartDao
import com.tunahan.ecommerceapp.room.FavoriteDao
import javax.inject.Inject

class HomeRepository @Inject constructor(private val favoriteDao: FavoriteDao,
private val cartDao: CartDao) {

    val readAllFavorite: LiveData<List<Favorite>> = favoriteDao.readAllFavorite()
    val readAllCart: LiveData<List<Cart>> = cartDao.readAllCart()
    suspend fun addFavorite(favorite: Favorite) {
        favoriteDao.addFavorite(favorite)
    }

    suspend fun updateFavorite(favorite: Favorite){
        favoriteDao.updateFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }

    suspend fun addCart(cart: Cart){
        cartDao.addCart(cart)
    }

    suspend fun updateCart(cart: Cart){
        cartDao.updateCart(cart)
    }

    suspend fun deleteCart(cart: Cart){
        cartDao.deleteCart(cart)
    }

}