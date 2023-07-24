package com.tunahan.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository):ViewModel() {

     val readAllData: LiveData<List<Favorite>> = homeRepository.readAllData

    fun addNote(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.addNote(favorite)
        }
    }

    fun updateDocument(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.updateDocument(favorite)
        }
    }

    fun deleteNote(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deleteNote(favorite)
        }
    }


}