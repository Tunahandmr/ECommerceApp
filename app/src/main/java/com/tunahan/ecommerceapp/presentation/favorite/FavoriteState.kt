package com.tunahan.ecommerceapp.presentation.favorite

import com.tunahan.ecommerceapp.domain.model.Favorite

data class FavoriteState(
    val isLoading: Boolean = false,
    var favorites: List<Favorite> = emptyList(),
    val error: String = ""
)