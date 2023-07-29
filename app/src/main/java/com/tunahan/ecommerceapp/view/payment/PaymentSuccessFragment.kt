package com.tunahan.ecommerceapp.view.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.tunahan.ecommerceapp.databinding.FragmentPaymentSuccessBinding

class PaymentSuccessFragment : Fragment() {

    private var _binding: FragmentPaymentSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.continueShoppingButton.setOnClickListener {
            findNavController().navigate(PaymentSuccessFragmentDirections.actionPaymentSuccessFragmentToHomeFragment())

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}