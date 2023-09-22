package com.tunahan.ecommerceapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.use_case.cart.AddCartUseCase
import com.tunahan.ecommerceapp.domain.use_case.favorite.AddFavoriteUseCase
import com.tunahan.ecommerceapp.domain.use_case.favorite.DeleteByIdFavoriteUseCase
import com.tunahan.ecommerceapp.domain.use_case.favorite.ReadAllFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailsViewModel @Inject constructor(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val readAllFavoriteUseCase: ReadAllFavoriteUseCase,
    private val deleteByIdFavoriteUseCase: DeleteByIdFavoriteUseCase,
    private val addCartUseCase: AddCartUseCase
):ViewModel(){

    /*private val _readAllFavorite = MutableLiveData<List<Favorite>>()
    val readAllFavorite: LiveData<List<Favorite>> = _readAllFavorite*/

    private val _readAllFavorite = MutableStateFlow(HomeDetailsState())
    val readAllFavorite: StateFlow<HomeDetailsState> = _readAllFavorite

    init {
        readAllFavorite()
    }

    private fun readAllFavorite() {
        viewModelScope.launch {
            readAllFavoriteUseCase().collect {
                _readAllFavorite.value = HomeDetailsState(it)
            }
        }
    }
    fun addCart(cart: Cart){
        viewModelScope.launch {
            addCartUseCase(cart)
        }
    }

    fun addFavorite(favorite: Favorite){
        viewModelScope.launch {
            addFavoriteUseCase(favorite)
        }
    }

    fun deleteByIdFavorite(favorite: Favorite) {
        viewModelScope.launch {
            deleteByIdFavoriteUseCase(favorite)
        }
    }

}