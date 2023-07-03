package com.tunahan.ecommerceapp.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.FragmentAdminBinding


class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adminAddFAB.setOnClickListener {
            findNavController().navigate(AdminFragmentDirections.actionAdminFragmentToAdminAddFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}