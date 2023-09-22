package com.tunahan.ecommerceapp.domain.use_case.sign

import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import javax.inject.Inject

class CheckCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(): Boolean = firebaseRepository.isCurrentUserExist()
}