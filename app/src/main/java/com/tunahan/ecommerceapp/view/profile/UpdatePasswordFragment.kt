package com.tunahan.ecommerceapp.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.FragmentUpdatePasswordBinding


class UpdatePasswordFragment : Fragment() {

    private var _binding: FragmentUpdatePasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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

        binding.saveButton.setOnClickListener {
            val user = auth.currentUser
            val newPassword = binding.newPassword.text.toString()

            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(),"Update password succesfully",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(UpdatePasswordFragmentDirections.actionUpdatePasswordFragmentToProfileFragment())

                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}