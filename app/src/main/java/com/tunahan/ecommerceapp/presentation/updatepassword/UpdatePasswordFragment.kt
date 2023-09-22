package com.tunahan.ecommerceapp.presentation.updatepassword

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.databinding.FragmentUpdatePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdatePasswordFragment : Fragment() {

    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val updatePasswordViewModel by viewModels<UpdatePasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flowObserve()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigate(UpdatePasswordFragmentDirections.actionUpdatePasswordFragmentToProfileFragment())
        }

        binding.savePasswordButton.setOnClickListener {
           // val user = auth.currentUser
          //  val currentPassword = user
            val currentPassword = binding.currentPasswordET.text.toString()
            val newPassword = binding.newPasswordET.text.toString()

            updatePasswordViewModel.updatePassword("tunahandemir818@gmail.com",currentPassword,newPassword)


          /*  user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(),"Update password succesfully",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(UpdatePasswordFragmentDirections.actionUpdatePasswordFragmentToProfileFragment())

                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                }*/
        }


    }

    private fun flowObserve(){

      /*  lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                updatePasswordViewModel.currentUser.collect{
                    when(it.currentUser){
                        is Resource.Success->{
                            val userEmail = it.currentUser.data.email
                            val userUid = it.currentUser.data.uid
                            val userFullname = it.currentUser.data.fullName

                            val newPassword = binding.newPasswordET.text.toString()
                            updatePasswordViewModel.updatePassword(newPassword,it)

                        }

                        is Resource.Error->{}

                        is Resource.Loading->{}
                    }
                }
            }
        }
*/

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                updatePasswordViewModel.updatePassword.collect{
                    when(it.password){
                        is Resource.Success->{
                            Toast.makeText(requireContext(),"Update password successfully",Toast.LENGTH_SHORT).show()
                            findNavController().navigate(UpdatePasswordFragmentDirections.actionUpdatePasswordFragmentToProfileFragment())
                        }
                        is Resource.Error->{
                            Snackbar.make(requireView(),it.password.throwable.message.toString(),Snackbar.LENGTH_LONG).setAction("Ok"){}.show()
                            Log.d("paslog",it.password.throwable.message.toString())
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