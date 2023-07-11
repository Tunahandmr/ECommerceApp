package com.tunahan.ecommerceapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tunahan.ecommerceapp.model.Document

@Database(entities = [Document::class], version = 1, exportSchema = false)
abstract class DocumentDatabase:RoomDatabase() {

    abstract fun documentDao():DocumentDao

}