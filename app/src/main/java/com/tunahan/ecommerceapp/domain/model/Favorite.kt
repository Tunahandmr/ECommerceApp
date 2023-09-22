package com.tunahan.ecommerceapp.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite_tables")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val bookId: String,
    val bookName:String,
    val imageUrl:String,
    val writer:String,
    val price:String
)
