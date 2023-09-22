package com.tunahan.ecommerceapp.domain.use_case.cart

import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import javax.inject.Inject

class DeleteAllCartUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {

    suspend operator fun invoke() {
        localDatabaseRepository.deleteAllCarts()
    }

}