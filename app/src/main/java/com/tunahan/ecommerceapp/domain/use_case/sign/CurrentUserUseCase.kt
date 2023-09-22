package com.tunahan.ecommerceapp.domain.use_case.sign

import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.model.User
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<User>> {

        return flow {
            emit(Resource.Loading)
            try {
                emit(Resource.Success(firebaseRepository.getCurrentUser()))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

}