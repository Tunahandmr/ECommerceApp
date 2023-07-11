package com.tunahan.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.model.Document
import com.tunahan.ecommerceapp.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository):ViewModel() {

     val readAllData: LiveData<List<Document>> = homeRepository.readAllData

    fun addNote(document: Document) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.addNote(document)
        }
    }

    fun updateDocument(document: Document){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.updateDocument(document)
        }
    }

    fun deleteNote(document: Document) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deleteNote(document)
        }
    }

}