package com.tunahan.ecommerceapp.presentation.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.FragmentPaymentBinding
import com.tunahan.ecommerceapp.common.CreditCardTextFormatter
import com.tunahan.ecommerceapp.common.isNullorEmpty
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel


class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            backButton.setOnClickListener {
                findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToCartFragment())
            }

            cardNumberET.addTextChangedListener(CreditCardTextFormatter())
            payNowButton.setOnClickListener {
                if (checkInfos(cardHolderET,cardNumberET,monthET,yearET,cvcCodeET,adressET)){
                    findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToPaymentSuccessFragment())
                    homeViewModel.deleteAllCarts()
                }

            }
        }

    }
    private fun checkInfos(
        cardHolderName: TextInputEditText,
        creditCardNumber: TextInputEditText,
        month: TextInputEditText,
        year: TextInputEditText,
        cvcCode: TextInputEditText,
        address: TextInputEditText,
    ): Boolean {
        val checkInfos = when {
            cardHolderName.isNullorEmpty(getString(R.string.warning_card_holdername)).not() -> false
            creditCardNumber.isNullorEmpty(getString(R.string.warning_card_number))
                .not() -> false
            month.isNullorEmpty(getString(R.string.warning_month)).not() -> false
            year.isNullorEmpty(getString(R.string.warning_year)).not() -> false
            cvcCode.isNullorEmpty(getString(R.string.warning_cvc)).not() -> false
            address.isNullorEmpty(getString(R.string.warning_adress)).not() -> false
            else -> true
        }
        return checkInfos
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}