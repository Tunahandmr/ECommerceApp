package com.tunahan.ecommerceapp.presentation.forgotpassword

import com.tunahan.ecommerceapp.common.Resource

data class ForgotPasswordState (
    val requestPassword: Resource<Void> = Resource.Loading
)