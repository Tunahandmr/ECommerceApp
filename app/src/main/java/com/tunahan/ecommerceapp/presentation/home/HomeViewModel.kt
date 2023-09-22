package com.tunahan.ecommerceapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.use_case.cart.AddCartUseCase
import com.tunahan.ecommerceapp.domain.use_case.favorite.ReadAllFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addCartUseCase: AddCartUseCase,
) :ViewModel(){


    private val _bookIsEmpty = MutableStateFlow(HomeState())
    val bookIsEmpty: StateFlow<HomeState> = _bookIsEmpty

    private fun bookIsEmpty(bookList: List<Favorite>) {
        _bookIsEmpty.value = HomeState(isLoading = bookList.isNullOrEmpty())

    }

    fun addCart(cart: Cart){
        viewModelScope.launch {
            addCartUseCase(cart)
        }
    }

}