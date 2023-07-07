package com.tunahan.ecommerceapp.view.admin

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ZygotePreload
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminAddBinding
import java.io.ByteArrayOutputStream
import java.util.UUID


class AdminAddFragment : Fragment() {

    private var _binding: FragmentAdminAddBinding? = null
    private val binding get() = _binding!!

    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    var category = ""

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture: Uri? = null
    private var selectedBitmap: Bitmap? = null
    
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddBinding.inflate(inflater, container, false)
        bookCategoryAdapter =
            BookCategoryAdapter(mList, object : BookCategoryAdapter.OnClickListener {
                override fun onClick(item: String) {
                    category = item
                }

            })
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdd()

        registerLauncher()
        binding.addImage.setOnClickListener {
            imageClick()
        }

        binding.saveBookButton.setOnClickListener {
            uploadProduct()
        }


    }


    private fun uploadProduct() {
        if (selectedPicture != null) {
            /*
            val smallBitmap = makeSmallerImage(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()*/

            val reference = storage.reference
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val imageReference = reference.child("images").child(imageName)

            imageReference.putFile(selectedPicture!!).addOnSuccessListener { task ->
                //get url
                val loadImageReference = reference.child("images").child(imageName)

                loadImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val dowloadUrl = uri.toString()
                    val date = Timestamp.now()
                    val bookName = binding.bookNameText.text.toString()
                    val price = binding.priceText.text.toString()
                    val writer = binding.writerText.text.toString()
                    val publisher = binding.publisherText.text.toString()
                    val pageCount = binding.pageCountText.text.toString()
                    val publicationYear = binding.publicationYearText.text.toString()
                    val language = binding.languageText.text.toString()

                    val addMap = hashMapOf<String, Any>()

                    addMap["date"] = date
                    addMap["bookName"] = bookName
                    addMap["price"] = price
                    addMap["writer"] = writer
                    addMap["publisher"] = publisher
                    addMap["pageCount"] = pageCount
                    addMap["publicationYear"] = publicationYear
                    addMap["language"] = language
                    addMap["category"] = category
                    addMap["downloadUrl"] = dowloadUrl


                    db.collection("books").add(addMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            findNavController().navigate(AdminAddFragmentDirections.actionAdminAddFragmentToAdminFragment())
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(
                            requireContext(),
                            exception.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }


        /*

         */
    }

    //permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun imageClick() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(
                        requireView(),
                        "Permission needed for Gallery!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Give Permission", View.OnClickListener {
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)

                }
            } else {

                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        requireView(),
                        "Permission needed for Gallery!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Give Permission", View.OnClickListener {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                }
            } else {

                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }

    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val intentFromResult = it.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        /* selectedPicture?.let {
                             binding.addImage.setImageURI(it)
                         }*/

                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    selectedPicture!!
                                )
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.addImage.setImageBitmap(selectedBitmap)

                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    selectedPicture
                                )
                                binding.addImage.setImageBitmap(selectedBitmap)

                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }

            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {

                    Toast.makeText(requireContext(), "Permission Needed!", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun makeSmallerImage(image: Bitmap, maximumSize: Int): Bitmap {

        var width = image.width
        var height = image.height

        val bitmapRatio: Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeigt = width / bitmapRatio
            height = scaledHeigt.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
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