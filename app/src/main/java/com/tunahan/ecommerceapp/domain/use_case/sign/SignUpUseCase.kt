package com.tunahan.ecommerceapp.domain.use_case.sign

import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.model.User
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading)
            try {
                emit(Resource.Success(firebaseRepository.signUpWithEmailAndPassword(email, password)))

            } catch (e: Exception) {
               emit( Resource.Error(e))
            }
        }
    }

}