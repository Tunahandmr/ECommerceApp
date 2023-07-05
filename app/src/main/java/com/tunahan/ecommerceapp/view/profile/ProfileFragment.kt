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
import com.tunahan.ecommerceapp.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser
        if (user?.email=="admin@gmail.com"){
            binding.addProductTextButton.visibility = View.VISIBLE
        }
        binding.addProductTextButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAdminFragment())
        }

        binding.userNameText.text = user?.displayName ?: "not username"
        binding.emailText.text = user?.email

        binding.updatePasswordTextButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())
        }

        binding.exitTextButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
        }

        binding.deleteUserTextButton.setOnClickListener {
            val user = auth.currentUser!!
            user.delete().addOnCompleteListener {task->
                if (task.isSuccessful){
                    Toast.makeText(requireContext(),"User delete succesfully",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSignInFragment())
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