package com.tunahan.ecommerceapp.presentation.profile

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
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flowObserve()
    }

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


        binding.addProductTextButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAdminFragment())
        }

        binding.updatePasswordTextButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())
        }

        binding.exitTextButton.setOnClickListener {
            profileViewModel.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
        }



        binding.deleteUserTextButton.setOnClickListener {

            profileViewModel.deleteUser()

        }
    }



    private fun flowObserve(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                profileViewModel.currentUser.collect{
                    when(it.currentUser){
                        is Resource.Success->{
                            val userEmail = it.currentUser.data.email
                            val userUid = it.currentUser.data.uid
                            val userFullname = it.currentUser.data.fullName
                            binding.emailText.text = userEmail
                            binding.userNameText.text = userFullname

                            profileViewModel.uidCheck(userUid)

                        }

                        is Resource.Error->{}

                        is Resource.Loading->{}
                    }
                }

            }
        }



        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                profileViewModel.uidCheck.collect{
                    if (it.isBoolean){
                        binding.addProductTextButton.visibility = View.VISIBLE
                    }else{
                        binding.addProductTextButton.visibility = View.GONE
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                profileViewModel.deleteUser.collect{
                   when(it.deleteUser){
                       is Resource.Success->{
                           findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
                       }
                       is Resource.Error->{
                           Snackbar.make(requireView(),it.deleteUser.throwable.message.toString(),Snackbar.LENGTH_LONG).setAction("Ok"){}.show()
                       }
                       is Resource.Loading->{}
                   }
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}