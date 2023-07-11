package com.tunahan.ecommerceapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tunahan.ecommerceapp.model.Document

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(document: Document)

    @Update
    suspend fun updateDocument(document: Document)

    @Delete
    suspend fun deleteNote(document: Document)

    @Query("SELECT * FROM document_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Document>>
}