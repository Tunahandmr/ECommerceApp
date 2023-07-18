package com.tunahan.ecommerceapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tunahan.ecommerceapp.model.Document
import com.tunahan.ecommerceapp.model.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase:RoomDatabase() {

    abstract fun favoriteDao():FavoriteDao

}