package com.tunahan.ecommerceapp.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite_tables")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val bookId: String,
    val bookName:String,
    val imageUrl:String,
    val writer:String,
    val publisher:String,
    val price:String
): Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}
