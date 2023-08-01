package com.tunahan.ecommerceapp.data.repository

import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.data.local.dao.CartDao
import com.tunahan.ecommerceapp.data.local.dao.FavoriteDao
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import javax.inject.Inject

class LocalDatabaseRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val cartDao: CartDao
) : LocalDatabaseRepository {
    override suspend fun addFavorite(favorite: Favorite) {
        favoriteDao.addFavorite(favorite)
    }

    override suspend fun updateFavorite(favorite: Favorite) {
        favoriteDao.updateFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }

    override suspend fun addCart(cart: Cart) {
        cartDao.addCart(cart)
    }

    override suspend fun updateCart(cart: Cart) {
        cartDao.updateCart(cart)
    }

    override suspend fun deleteCart(cart: Cart) {
        cartDao.deleteCart(cart)
    }

    override suspend fun deleteAllCarts() {
        cartDao.deleteAllCarts()
    }
}