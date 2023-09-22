package com.tunahan.ecommerceapp.presentation.home

import com.tunahan.ecommerceapp.domain.model.Favorite

data class HomeState(
    val isLoading:Boolean = false,
    var favorites: List<Favorite> = emptyList()
)