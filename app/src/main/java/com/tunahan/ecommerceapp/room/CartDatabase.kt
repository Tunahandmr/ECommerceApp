package com.tunahan.ecommerceapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tunahan.ecommerceapp.model.Cart

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase: RoomDatabase() {
    abstract fun cartDao():CartDao
}