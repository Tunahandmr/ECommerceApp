package com.tunahan.ecommerceapp.domain.use_case.sign

import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import javax.inject.Inject

class LiveUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {

    suspend operator fun invoke(email: String): Resource<Void> {

        return try {
            Resource.Loading
            Resource.Success(firebaseRepository.sendPasswordResetEmail(email))
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

}