package com.tunahan.ecommerceapp.presentation.details

import com.tunahan.ecommerceapp.domain.model.Favorite

data class HomeDetailsState(var favorites: List<Favorite> = emptyList())