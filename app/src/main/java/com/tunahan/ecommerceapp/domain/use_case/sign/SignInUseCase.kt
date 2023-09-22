package com.tunahan.ecommerceapp.domain.use_case.sign

import android.content.Context
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {

      operator fun invoke(
        email: String,
        password: String):Flow<Resource<FirebaseUser>> {

         return flow {
             emit(Resource.Loading)
             try {
                 val user = firebaseRepository.signInWithEmailAndPassword(email, password).user
                 if (user != null) {
                     emit(Resource.Success(user))
                 } else {
                     emit(Resource.Error(Exception("User is null")))
                 }
             } catch (e: Exception) {
                 emit(Resource.Error(e))
             }
         }
    }

}