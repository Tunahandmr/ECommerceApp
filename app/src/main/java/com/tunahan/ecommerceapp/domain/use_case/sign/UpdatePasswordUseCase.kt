package com.tunahan.ecommerceapp.domain.use_case.sign

import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(email: String,currentPassword:String, newpassword: String): Flow<Resource<Void>> {
        return flow {
            emit(Resource.Loading)
            try {
                val user = firebaseRepository.signInWithEmailAndPassword(email, currentPassword).user
                if (user != null) {
                    emit(Resource.Success(firebaseRepository.updatePassword(newpassword)))
                } else {
                    emit(Resource.Error(Exception("Error")))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }


    }

}