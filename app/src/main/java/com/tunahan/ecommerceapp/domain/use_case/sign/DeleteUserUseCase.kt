package com.tunahan.ecommerceapp.domain.use_case.sign

import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {

     operator fun invoke(): Flow<Resource<Void>> {

        return flow {
            emit(Resource.Loading)
            try {
                emit(Resource.Success(firebaseRepository.deleteUser()))

            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

}