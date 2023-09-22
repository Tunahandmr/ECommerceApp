package com.tunahan.ecommerceapp.presentation.updatepassword

import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource

data class UpdatePasswordState(
    val password:Resource<Void> = Resource.Loading,
    val currentUser: Resource<FirebaseUser> = Resource.Loading
)