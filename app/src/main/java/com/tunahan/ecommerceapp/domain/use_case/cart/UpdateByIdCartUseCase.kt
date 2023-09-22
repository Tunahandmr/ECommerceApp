package com.tunahan.ecommerceapp.domain.use_case.cart

import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import javax.inject.Inject

class UpdateByIdCartUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
){
    suspend operator fun invoke(cart: Cart){
        localDatabaseRepository.updateCart(cart)
    }

}