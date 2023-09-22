package com.tunahan.ecommerceapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tunahan.ecommerceapp.data.local.dao.CartDao
import com.tunahan.ecommerceapp.domain.model.Cart

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}