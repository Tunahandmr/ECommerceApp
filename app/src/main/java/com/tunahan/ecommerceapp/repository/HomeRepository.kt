package com.tunahan.ecommerceapp.repository

import androidx.lifecycle.LiveData
import com.tunahan.ecommerceapp.model.Document
import com.tunahan.ecommerceapp.room.DocumentDao
import javax.inject.Inject

class HomeRepository @Inject constructor(private val documentDao: DocumentDao) {

    val readAllData: LiveData<List<Document>> = documentDao.readAllData()

    suspend fun addNote(document: Document) {
        documentDao.addNote(document)
    }

    suspend fun updateDocument(document: Document){
        documentDao.updateDocument(document)
    }

    suspend fun deleteNote(document: Document) {
        documentDao.deleteNote(document)
    }

}