package com.tunahan.ecommerceapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tunahan.ecommerceapp.model.Cart
import com.tunahan.ecommerceapp.model.Favorite

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCart(cart: Cart)

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Query("SELECT * FROM cart_tables ORDER BY id ASC")
    fun readAllCart(): LiveData<List<Cart>>

    @Query("DELETE FROM cart_tables")
    fun deleteAllCarts()
}