package com.tunahan.ecommerceapp.presentation.admin.update

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.presentation.home.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminUpdateBinding
import com.tunahan.ecommerceapp.domain.model.Product
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel
import java.util.UUID


class AdminUpdateFragment : Fragment() {


    private var _binding: FragmentAdminUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var mHomeViewModel: HomeViewModel
    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    private var productList = ArrayList<Product>()
    private val args by navArgs<AdminUpdateFragmentArgs>()
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    var myCategory = ""

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var selectedPicture: Uri? = null
    private var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUpdateBinding.inflate(inflater, container, false)
        mHomeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        readData(args.documentUuid)

        binding.updateBookButton.setOnClickListener {
            updateData(args.documentUuid)
        }

        binding.deleteBookButton.setOnClickListener {
            deleteData(args.documentUuid)
        }

        registerLauncher()

        binding.addImage.setOnClickListener {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }



        listAdd()
        bookCategoryAdapter =
            BookCategoryAdapter(mList, object : BookCategoryAdapter.OnClickListener {
                override fun onClick(item: String) {
                    myCategory = item
                }
            })
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter
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
                val publisher = document?.get("publisher") as String?
                val pageCount = document?.get("pageCount") as String?
                val publicationYear = document?.get("publicationYear") as String?
                val language = document?.get("language") as String?
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
                binding.bookNameText.setText(bookName)
                binding.priceText.setText(price)
                binding.writerText.setText(writer)
                binding.publisherText.setText(publisher)
                binding.pageCountText.setText(pageCount)
                binding.publicationYearText.setText(publicationYear)
                binding.languageText.setText(language)
                Glide.with(requireContext()).load(url).into(binding.addImage)

            }

        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateData(docId: String) {
        val addMap = hashMapOf<String, Any>()

        if (selectedPicture != null) {
            val reference = storage.reference
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val imageReference = reference.child("images").child(imageName)

            imageReference.putFile(selectedPicture!!).addOnSuccessListener { task ->
                //get url
                val loadImageReference = reference.child("images").child(imageName)

                loadImageReference.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()

                    val date = Timestamp.now()
                    val bookName = binding.bookNameText.text.toString()
                    val price = binding.priceText.text.toString()
                    val writer = binding.writerText.text.toString()
                    val publisher = binding.publisherText.text.toString()
                    val pageCount = binding.pageCountText.text.toString()
                    val publicationYear = binding.publicationYearText.text.toString()
                    val language = binding.languageText.text.toString()

                    addMap["date"] = date
                    addMap["bookName"] = bookName
                    addMap["price"] = price
                    addMap["writer"] = writer
                    addMap["publisher"] = publisher
                    addMap["pageCount"] = pageCount
                    addMap["publicationYear"] = publicationYear
                    addMap["language"] = language
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
            val bookName = binding.bookNameText.text.toString()
            val price = binding.priceText.text.toString()
            val writer = binding.writerText.text.toString()
            val publisher = binding.publisherText.text.toString()
            val pageCount = binding.pageCountText.text.toString()
            val publicationYear = binding.publicationYearText.text.toString()
            val language = binding.languageText.text.toString()

            addMap["date"] = date
            addMap["bookName"] = bookName
            addMap["price"] = price
            addMap["writer"] = writer
            addMap["publisher"] = publisher
            addMap["pageCount"] = pageCount
            addMap["publicationYear"] = publicationYear
            addMap["language"] = language
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

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val intentFromResult = it.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data

                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver, selectedPicture!!
                                )
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.addImage.setImageBitmap(selectedBitmap)

                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver, selectedPicture
                                )
                                binding.addImage.setImageBitmap(selectedBitmap)

                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
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