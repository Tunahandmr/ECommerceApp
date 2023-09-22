package com.tunahan.ecommerceapp.domain.repository

import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

interface LocalDatabaseRepository {

    fun readAllCart() : Flow<List<Cart>>

    fun readAllFavorite() : Flow<List<Favorite>>
    suspend fun addFavorite(favorite: Favorite)

    suspend fun updateFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    suspend fun addCart(cart: Cart)

    suspend fun updateCart(cart: Cart)

    suspend fun deleteCart(cart: Cart)

    fun deleteAllCarts()
}