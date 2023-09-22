package com.tunahan.ecommerceapp.presentation.signin

import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource

data class SignInState(
    val isUser:Resource<FirebaseUser> = Resource.Loading
)
