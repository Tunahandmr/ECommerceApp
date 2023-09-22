package com.tunahan.ecommerceapp.domain.use_case.cart

import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import javax.inject.Inject

class DeleteByIdCartUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {
    suspend operator fun invoke(cart: Cart){
        localDatabaseRepository.deleteCart(cart)
    }

}