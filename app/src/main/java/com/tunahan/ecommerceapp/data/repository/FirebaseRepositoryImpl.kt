package com.tunahan.ecommerceapp.data.repository

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.model.Image
import com.tunahan.ecommerceapp.domain.model.Product
import com.tunahan.ecommerceapp.domain.model.User
import com.tunahan.ecommerceapp.domain.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage:FirebaseStorage
) : FirebaseRepository {


    override fun getBooksFromFirestore(): Flow<Resource<List<Product>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addBookToFirestore(title: String, author: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBookFromFirestore(bookId: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getFirebaseUserUid(): String {
        return auth.currentUser?.uid.orEmpty()
    }

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()

    }


    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendPasswordResetEmail(email: String): Void {
        return auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun updatePassword(password: String):Void {
       return auth.currentUser!!.updatePassword(password).await()
    }

    override suspend fun isCurrentUserExist(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getCurrentUser(): User {
        return User(
            auth.currentUser?.email.toString(), auth.currentUser?.displayName.toString(),auth.currentUser?.uid.toString()
        )
    }

    override suspend fun deleteUser(): Void {
        return auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}