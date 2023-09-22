package com.tunahan.ecommerceapp.domain.use_case.sign

import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke() = firebaseRepository.signOut()

}