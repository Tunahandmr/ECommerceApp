package com.tunahan.ecommerceapp.presentation.cart

import com.tunahan.ecommerceapp.domain.model.Cart

data class CartState (
    val isLoading:Boolean = false,
    var carts: List<Cart> = emptyList()
)