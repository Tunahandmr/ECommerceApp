package com.tunahan.ecommerceapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tunahan.ecommerceapp.data.local.dao.FavoriteDao
import com.tunahan.ecommerceapp.domain.model.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase:RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}