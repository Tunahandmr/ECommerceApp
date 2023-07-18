package com.tunahan.ecommerceapp.view.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.FragmentProfileBinding
import com.tunahan.ecommerceapp.model.Product
import com.tunahan.ecommerceapp.view.MainActivity
import com.tunahan.ecommerceapp.view.admin.AdminAddFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture: Uri? = null
    private var selectedBitmap: Bitmap? = null
    var myImage:ImageView?=null

    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser
        if (user?.email == "admin@gmail.com") {
            binding.addProductTextButton.visibility = View.VISIBLE
        }
        binding.addProductTextButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAdminFragment())
        }

        binding.userNameText.text = user?.displayName ?: "not username"
        binding.emailText.text = user?.email

        readData()
        binding.updatePasswordTextButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())
        }

        binding.exitTextButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
        }

        registerLauncher()

        binding.changeProfileTextButton.setOnClickListener {
            showCustomDiaLogBox()
        }

        binding.deleteUserTextButton.setOnClickListener {
            val user = auth.currentUser!!
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "User delete succesfully", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun readData() {

            val userId = auth.currentUser?.uid.toString()
            val docRef = db.collection("profile").document(userId)

            docRef.addSnapshotListener { value, error ->

                if (error != null) {
                    Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
                }else{
                    if (value != null){

                        val document = value.data

                        val url = document?.get("downloadUrl") as String?
                        if (url != null) {
                            Glide.with(requireContext()).load(url).circleCrop().into(binding.profileIV)
                        }
                    }
                }

            }
    }

    private fun showCustomDiaLogBox() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_change_profile)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val saveImage:ImageView = dialog.findViewById(R.id.saveButton)
        val clickmage:ImageView = dialog.findViewById(R.id.profileImage)

        myImage=clickmage
        saveImage.setOnClickListener {
            uploadProfileImage()
            dialog.cancel()
        }

        clickmage.setOnClickListener {
            imageClick()
        }

        dialog.show()
    }


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
                if (it.resultCode == Activity.RESULT_OK) {
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
                                myImage?.setImageBitmap(selectedBitmap)

                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    selectedPicture
                                )
                                myImage?.setImageBitmap(selectedBitmap)

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

    private fun uploadProfileImage() {
        if (selectedPicture != null) {

            val reference = storage.reference
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val imageReference = reference.child("profileImages").child(imageName)

            imageReference.putFile(selectedPicture!!).addOnSuccessListener { task ->
                //get url
                val loadImageReference = reference.child("profileImages").child(imageName)

                loadImageReference.downloadUrl.addOnSuccessListener { uri ->
                    val dowloadUrl = uri.toString()

                    // val bookUuid = UUID.randomUUID()
                    val addMap = hashMapOf<String, Any>()

                    addMap["downloadUrl"] = dowloadUrl
                    //  addMap["bookUuid"] = bookUuid.toString()
                    val userId  = auth.currentUser?.uid.toString()

                    db.collection("profile").document(userId).set(addMap)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                Toast.makeText(requireContext(), "Profile image change succesfully", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}