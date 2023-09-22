package com.tunahan.ecommerceapp.domain.use_case.cart

import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.repository.LocalDatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadCartUseCase @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository
) {

    suspend operator fun invoke(): Flow<List<Cart>>{

        return localDatabaseRepository.readAllCart()

    }

}