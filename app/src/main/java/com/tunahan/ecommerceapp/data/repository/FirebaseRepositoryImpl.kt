package com.tunahan.ecommerceapp.data.repository

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
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
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.common.UiState
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
    override fun getBooks(categoryFilter: String,result: (UiState<List<Product>>) -> Unit) {
        firestore.collection("books")
            .whereEqualTo("category", categoryFilter)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val products = arrayListOf<Product>()
                            val documents = value.documents

                            products.clear()

                            for (document in documents) {

                                val url = document.get("downloadUrl") as String?
                                val bookName = document.get("bookName") as String?
                                val price = document.get("price") as String?
                                val writer = document.get("writer") as String?
                                val pageCount = document.get("pageCount") as String?
                                val explanation = document.get("explanation") as String?
                                val bookUuid = document?.get("bookUuid") as String?

                                val readProduct = Product(
                                    bookUuid,
                                    url,
                                    bookName,
                                    price,
                                    writer,
                                    pageCount,
                                    explanation,
                                    true
                                )

                                products.add(readProduct)
                            }

                            result.invoke(
                                UiState.Success(products)
                            )
                          //  homeProductAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

         /*   .get()
            .addOnSuccessListener {
                val notes = arrayListOf<Product>()
                for (document in it) {
                    val note = document.toObject(Product::class.java)
                    notes.add(note)
                } result.invoke(
                    UiState.Success(notes)
                )

            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }*/
    }

    override fun addBook(product: Product, result: (UiState<Pair<Product, String>>) -> Unit) {
        val document = firestore.collection("books").document()
        //product.id = document.id
        document
            .set(product)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(product, "Note has been created successfully"))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateBook(product: Product,docId: String, result: (UiState<String>) -> Unit) {
        val document = firestore.collection("books").document(docId)
        document
            .set(product)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been update successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun deleteBook(product: Product,docId: String, result: (UiState<String>) -> Unit) {
        firestore.collection("books").document(docId)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Note successfully deleted!"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure(e.message))
            }
    }


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