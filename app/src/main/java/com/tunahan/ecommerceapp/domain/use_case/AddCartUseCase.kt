package com.tunahan.ecommerceapp.domain.use_case

import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import java.lang.Exception
import javax.inject.Inject

class AddCartUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {
    suspend operator fun invoke(cart: Cart) {
       localDatabaseRepository.addCart(cart)
    }
}