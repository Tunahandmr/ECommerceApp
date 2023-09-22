package com.tunahan.ecommerceapp.presentation.update

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.databinding.FragmentAdminUpdateBinding
import com.tunahan.ecommerceapp.presentation.home.BookCategoryAdapter
import com.tunahan.ecommerceapp.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AdminUpdateFragment : Fragment() {


    private var _binding: FragmentAdminUpdateBinding? = null
    private val binding get() = _binding!!

    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    private var productList = ArrayList<Product>()
    private val args by navArgs<AdminUpdateFragmentArgs>()
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    var myCategory = ""

    private lateinit var pickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedUri: Uri? = null
    private var bitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listAdd()
        bookCategoryAdapter =
            BookCategoryAdapter(mList)
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter

        bookCategoryAdapter.categoryFilter = {
            myCategory = it
        }

        readData(args.documentUuid)

        binding.updateBookButton.setOnClickListener {
            updateData(args.documentUuid)
        }

        binding.deleteBookButton.setOnClickListener {
            deleteData(args.documentUuid)
        }

        photoPicker()

        binding.addImage.setOnClickListener {
            pickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }


    }


    private fun photoPicker() {

        pickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

                selectedUri = uri

                uri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images
                            .Media.getBitmap(requireActivity().contentResolver, it)

                        binding.addImage.setImageBitmap(bitmap)
                    } else {
                        val source =
                            ImageDecoder.createSource(requireActivity().contentResolver, it)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        binding.addImage.setImageBitmap(bitmap)
                    }
                }

            }

    }

    private fun readData(docId: String) {
        val docRef = db.collection("books").document(
            docId
        )

        docRef.get().addOnSuccessListener {

            if (it != null) {
                productList.clear()
                val document = it.data

                val url = document?.get("downloadUrl") as String?
                val bookName = document?.get("bookName") as String?
                val price = document?.get("price") as String?
                val writer = document?.get("writer") as String?
                val pageCount = document?.get("pageCount") as String?
                val explanation = document?.get("explanation") as String?
                val category = document?.get("category") as String?
                val bookUuid = document?.get("bookUuid") as String?


                myCategory = category.toString()
                var myCounter: Int? = null

                when (category) {
                    "Classics" -> myCounter = 0
                    "Fantasy" -> myCounter = 1
                    "Horror" -> myCounter = 2
                    "Self-improvement" -> myCounter = 3
                    "History" -> myCounter = 4
                    "Philosophy" -> myCounter = 5

                }

                bookCategoryAdapter.setData(myCounter ?: 0)
                binding.bookNameET.setText(bookName)
                binding.priceET.setText(price)
                binding.writerET.setText(writer)
                binding.pageET.setText(pageCount)
                binding.explanationET.setText(explanation)
                Glide.with(requireContext()).load(url).into(binding.addImage)

            }

        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateData(docId: String) {
        val addMap = hashMapOf<String, Any>()

        if (selectedUri != null) {
            val reference = storage.reference
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val imageReference = reference.child("images").child(imageName)

            imageReference.putFile(selectedUri!!).addOnSuccessListener { task ->
                //get url
                val loadImageReference = reference.child("images").child(imageName)

                loadImageReference.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()

                    val date = Timestamp.now()
                    val bookName = binding.bookNameET.text.toString()
                    val price = binding.priceET.text.toString()
                    val writer = binding.writerET.text.toString()
                    val pageCount = binding.pageET.text.toString()
                    val explanation = binding.explanationET.text.toString()

                    addMap["date"] = date
                    addMap["bookName"] = bookName
                    addMap["price"] = price
                    addMap["writer"] = writer
                    addMap["pageCount"] = pageCount
                    addMap["explanation"] = explanation
                    addMap["category"] = myCategory
                    addMap["bookUuid"] = docId
                    addMap["downloadUrl"] = downloadUrl

                    db.collection("books").document(docId).update(addMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                findNavController().navigate(AdminUpdateFragmentDirections.actionAdminUpdateFragmentToAdminFragment())
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(
                                requireContext(), exception.localizedMessage, Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }else{
            val date = Timestamp.now()
            val bookName = binding.bookNameET.text.toString()
            val price = binding.priceET.text.toString()
            val writer = binding.writerET.text.toString()
            val pageCount = binding.pageET.text.toString()
            val explanation = binding.explanationET.text.toString()

            addMap["date"] = date
            addMap["bookName"] = bookName
            addMap["price"] = price
            addMap["writer"] = writer
            addMap["pageCount"] = pageCount
            addMap["explanation"] = explanation
            addMap["category"] = myCategory
            addMap["bookUuid"] = docId


            db.collection("books").document(docId).update(addMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        findNavController().navigate(AdminUpdateFragmentDirections.actionAdminUpdateFragmentToAdminFragment())
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        requireContext(), exception.localizedMessage, Toast.LENGTH_LONG
                    ).show()
                }
        }



    }


    private fun deleteData(docId: String) {
        db.collection("books").document(docId).delete()
            .addOnSuccessListener {
                findNavController().navigate(AdminUpdateFragmentDirections.actionAdminUpdateFragmentToAdminFragment())
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun listAdd() {
        mList.add("Classics")
        mList.add("Fantasy")
        mList.add("Horror")
        mList.add("Self-improvement")
        mList.add("History")
        mList.add("Philosophy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}