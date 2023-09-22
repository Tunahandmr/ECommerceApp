package com.tunahan.ecommerceapp.domain.use_case.sign

import com.bumptech.glide.load.HttpException
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(email: String): Flow<Resource<Void>> {

        return flow {
            try {
                emit(Resource.Loading)
                val result = firebaseRepository.sendPasswordResetEmail(email)
                if (result!=null){
                    emit(Resource.Success(result))
                }else{
                    emit(Resource.Error(Exception("Error!")))
                }
            }  catch (e: HttpException) {
                emit(Resource.Error(e))
            } catch (e: IOException) {
                emit(Resource.Error(e))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

}