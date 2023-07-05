package com.tunahan.ecommerceapp.view.sign

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {


    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.signupButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val username = binding.fullname.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }

                    currentUser?.let {
                        currentUser.updateProfile(profileUpdates).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("mytag", "User profile updated.")
                            }
                        }
                    }

                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
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