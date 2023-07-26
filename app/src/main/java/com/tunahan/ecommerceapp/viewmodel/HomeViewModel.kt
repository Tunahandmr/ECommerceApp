package com.tunahan.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.model.Cart
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    val readAllFavorite: LiveData<List<Favorite>> = homeRepository.readAllFavorite
    val readAllCart: LiveData<List<Cart>> = homeRepository.readAllCart

    fun addFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.addFavorite(favorite)
        }
    }

    fun updateFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.updateFavorite(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deleteFavorite(favorite)
        }
    }

    fun addCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.addCart(cart)
        }
    }

    fun updateCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.updateCart(cart)
        }
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.deleteCart(cart)
        }
    }


}