package com.tunahan.ecommerceapp.presentation.forgotpassword

import android.os.Bundle
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
import com.tunahan.ecommerceapp.common.isValidEmail
import com.tunahan.ecommerceapp.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val forgotPasswordViewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //flowObserve()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

      //  auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sentButton.setOnClickListener {
            val email = binding.emailET.text.toString()
           // if (binding.emailET.isValidEmail("Wrong email"))
                forgotPasswordViewModel.sendPasswordResetEmail(email)



            /*  Firebase.auth.sendPasswordResetEmail(email)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful) {
                          Toast.makeText(
                              requireContext(),
                              "Password reset request sent",
                              Toast.LENGTH_LONG
                          ).show()

                          findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment())
                      }
                  }.addOnFailureListener { exception ->
                      Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                          .show()
                  }*/
        }

        forgotPasswordViewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Password reset request sent",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Resource.Error -> {
                    Snackbar.make(
                        requireView(),
                        it.throwable.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).setAction("Ok") {}.show()
                }
                Resource.Loading ->{}
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment())
        }
    }

    private fun flowObserve() {/*
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                forgotPasswordViewModel.requestPassword.collect {
                    when (it.requestPassword) {
                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Password reset request sent",
                                Toast.LENGTH_LONG
                            ).show()

                            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment())
                        }

                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                it.requestPassword.throwable.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).setAction("Ok") {}.show()
                        }

                        is Resource.Loading -> {}
                    }
                }
            }
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}