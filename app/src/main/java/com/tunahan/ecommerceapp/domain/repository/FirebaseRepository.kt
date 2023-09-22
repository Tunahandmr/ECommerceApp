package com.tunahan.ecommerceapp.domain.repository

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.activity.result.ActivityResult
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.model.Image
import com.tunahan.ecommerceapp.domain.model.Product
import com.tunahan.ecommerceapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun getBooksFromFirestore(): Flow<Resource<List<Product>>>
    suspend fun addBookToFirestore(title: String, author: String): Resource<Boolean>
    suspend fun deleteBookFromFirestore(bookId: String): Resource<Boolean>
    suspend fun getFirebaseUserUid(): String
    suspend fun signUpWithEmailAndPassword(email: String, password: String)
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    suspend fun sendPasswordResetEmail(email: String): Void
    suspend fun updatePassword(password: String):Void
    suspend fun isCurrentUserExist(): Boolean
    suspend fun getCurrentUser(): User
    suspend fun deleteUser():Void
   suspend fun signOut()
}