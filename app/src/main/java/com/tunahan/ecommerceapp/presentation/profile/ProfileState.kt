package com.tunahan.ecommerceapp.presentation.profile

import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.model.User

data class ProfileState(
    val currentUser: Resource<User> = Resource.Loading,
    val isBoolean:Boolean = false,
    val deleteUser:Resource<Void> = Resource.Loading
)