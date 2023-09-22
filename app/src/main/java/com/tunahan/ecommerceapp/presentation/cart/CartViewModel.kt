package com.tunahan.ecommerceapp.presentation.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.use_case.cart.DeleteByIdCartUseCase
import com.tunahan.ecommerceapp.domain.use_case.cart.ReadCartUseCase
import com.tunahan.ecommerceapp.domain.use_case.cart.UpdateByIdCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val readCartUseCase:ReadCartUseCase,
    private val updateByIdCartUseCase: UpdateByIdCartUseCase,
    private val deleteByIdCartUseCase: DeleteByIdCartUseCase
):ViewModel() {
/*
    private val _readAllCart = MutableLiveData<List<Cart>>()
    val readAllCart: LiveData<List<Cart>> = _readAllCart
*/

    private val _readAllCart = MutableStateFlow(CartState())
    val readAllCart: StateFlow<CartState> = _readAllCart


    /*private val _cartIsEmpty = MutableLiveData<Boolean>()
    val cartIsEmpty: LiveData<Boolean> = _cartIsEmpty*/

    private val _cartIsEmpty = MutableStateFlow(CartState())
    val cartIsEmpty: StateFlow<CartState> = _cartIsEmpty

    init {
        readAllCart()
    }

    private fun cartIsEmpty(cartList: List<Cart>) {
        _cartIsEmpty.value = CartState(isLoading = cartList.isNullOrEmpty())

    }
    private fun readAllCart(){
        viewModelScope.launch {
            readCartUseCase().collect{
                _readAllCart.value = CartState(carts = it)
                cartIsEmpty(it)
            }
        }
    }

    fun updateByIdCart(cart: Cart){
        viewModelScope.launch {
            updateByIdCartUseCase(cart)
        }
    }

    fun deleteByIdCart(cart: Cart){
        viewModelScope.launch {
            deleteByIdCartUseCase(cart)
        }
    }
}