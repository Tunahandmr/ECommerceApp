package com.tunahan.ecommerceapp.presentation.signup

import com.tunahan.ecommerceapp.common.Resource

data class SignUpState(
    val signUpUser: Resource<Unit> = Resource.Loading
)