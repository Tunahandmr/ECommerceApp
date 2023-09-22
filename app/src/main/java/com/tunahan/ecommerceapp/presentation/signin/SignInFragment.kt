package com.tunahan.ecommerceapp.presentation.signin

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
import com.tunahan.ecommerceapp.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val signInViewModel by viewModels<SingInViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
           val isCurrentUser = signInViewModel.checkCurrentUser()
            if (isCurrentUser){
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment())
            }
        }

        flowObserve()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {

            val email = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInViewModel.signInWithEmailAndPassword(email, password)
            } else {
                Snackbar.make(requireView(),"Enter email or password",Snackbar.LENGTH_LONG).setAction("Ok"){}.show()
            }

        }

        binding.signupButton.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.forgotPasswordText.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment())
        }

    }

    private fun flowObserve() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                signInViewModel.result.collect{
                    when (it.isUser) {
                        is Resource.Success -> {
                            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment())
                        }
                        is Resource.Error -> {
                            Snackbar.make(requireView(),it.isUser.throwable.message.toString(),Snackbar.LENGTH_LONG).setAction("Ok"){}.show()
                        }
                       is Resource.Loading -> println("loading")

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