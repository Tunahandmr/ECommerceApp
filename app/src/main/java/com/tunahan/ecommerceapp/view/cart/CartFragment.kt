package com.tunahan.ecommerceapp.view.cart

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.adapter.CartAdapter
import com.tunahan.ecommerceapp.databinding.FragmentCartBinding
import com.tunahan.ecommerceapp.model.Cart
import com.tunahan.ecommerceapp.util.SwipeToDeleteCallback
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel


class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel:HomeViewModel
    private val cartAdapter by lazy { CartAdapter(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.readAllCart.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it)
            binding.cartRV.adapter = cartAdapter

            var totalPrice=0
            for (cartPrice in it){
                totalPrice+=cartPrice.price.toInt()
            }

            binding.totalPriceTV.text = "Total: $totalPrice ₺"
        })

        swipeToDelete()

    }


    private fun swipeToDelete() {

        val swipeCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val selectedFavorites = cartAdapter.carts[position]
                homeViewModel.deleteCart(selectedFavorites)
                cartAdapter.notifyItemRemoved(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.cartRV)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}