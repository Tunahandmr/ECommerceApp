package com.tunahan.ecommerceapp.presentation.add

import android.app.Activity.RESULT_OK
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.presentation.home.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminAddBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AdminAddFragment : Fragment() {

    private var _binding: FragmentAdminAddBinding? = null
    private val binding get() = _binding!!

    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    var category = ""


    private lateinit var pickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedUri: Uri? = null
    private var bitmap: Bitmap? = null

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddBinding.inflate(inflater, container, false)

        bookCategoryAdapter =
            BookCategoryAdapter(mList)
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdd()

        bookCategoryAdapter.categoryFilter = {
            category = it
        }

        photoPicker()

        binding.addImage.setOnClickListener {
            pickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.saveBookButton.setOnClickListener {
            uploadProduct()
        }


    }

    private fun uploadProduct() {
        if (selectedUri != null) {

            val reference = storage.reference
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val imageReference = reference.child("images").child(imageName)

            imageReference.putFile(selectedUri!!).addOnSuccessListener { task ->
                //get url
                val loadImageReference = reference.child("images").child(imageName)

                loadImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val dowloadUrl = uri.toString()
                    val date = Timestamp.now()
                    val bookName = binding.bookNameET.text.toString()
                    val price = binding.priceET.text.toString()
                    val writer = binding.writerET.text.toString()
                    val pageCount = binding.pageET.text.toString()
                    val explanation = binding.explanationET.text.toString()

                    val bookUuid = UUID.randomUUID()
                    val addMap = hashMapOf<String, Any>()

                    addMap["date"] = date
                    addMap["bookName"] = bookName
                    addMap["price"] = price
                    addMap["writer"] = writer
                    addMap["pageCount"] = pageCount
                    addMap["explanation"] = explanation
                    addMap["category"] = category
                    addMap["downloadUrl"] = dowloadUrl
                    addMap["bookUuid"] = bookUuid.toString()
                    addMap["bool"] = false



                    db.collection("books").document(bookUuid.toString()).set(addMap)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                // get document id
                                //val docID = task.result.id
                                // val document = Document(0, docID)
                                //  mHomeViewModel.addNote(document)

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

    private fun listAdd() {
        mList.add("")
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