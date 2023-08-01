package com.tunahan.ecommerceapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_tables")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val bookId: String,
    val bookName:String,
    val imageUrl:String,
    val writer:String,
    val price:String,
    val piece:Int
)
