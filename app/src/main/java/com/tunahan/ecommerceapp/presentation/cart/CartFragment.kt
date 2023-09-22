package com.tunahan.ecommerceapp.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tunahan.ecommerceapp.databinding.FragmentCartBinding
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.common.SwipeToDeleteCallback
import com.tunahan.ecommerceapp.presentation.cart.components.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartAdapter by lazy { CartAdapter(requireContext()) }
    private val cartViewModel: CartViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                cartViewModel.readAllCart.collect {
                    if (it!=null){
                        cartAdapter.setData(it.carts)
                        binding.cartRV.adapter = cartAdapter

                        var totalPrice=0
                        for (cartPrice in it.carts){
                            totalPrice+=cartPrice.price.toInt()
                        }

                        binding.totalPriceTV.text = "Total: $totalPrice ₺"
                    }

                }
            }
        }


        cartIsEmpty()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        swipeToDelete()

        clickControl()

        binding.orderNowButton.setOnClickListener {
            findNavController().navigate(CartFragmentDirections.actionCartFragmentToPaymentFragment())
        }

    }


    private fun cartIsEmpty(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                cartViewModel.cartIsEmpty.collect{
                    it.let {
                        if (it.isLoading){
                            binding.cartEmptyIV.visibility = View.VISIBLE
                            binding.cartEmptyTV.visibility = View.VISIBLE
                        }else{
                            binding.cartEmptyIV.visibility = View.GONE
                            binding.cartEmptyTV.visibility = View.GONE
                        }
                    }
                }
            }
        }


    }
    private fun clickControl(){

        cartAdapter.onIncreaseClick =  {id,price,piece,bId,bName,url,wrt->
            val onePiecePrice = price/(piece-1)
            val currentPrice = onePiecePrice*piece

            val cart = Cart(id,bId,bName,url,wrt,currentPrice.toString(),piece)
            cartViewModel.updateByIdCart(cart)
        }

        cartAdapter.onDecreaseClick = {id,price,piece,bId,bName,url,wrt->
            val onePiecePrice = price/(piece+1)
            val currentPrice = onePiecePrice*piece

            val cart = Cart(id,bId,bName,url,wrt,currentPrice.toString(),piece)
            cartViewModel.updateByIdCart(cart)
        }

        cartAdapter.onDeleteClick ={
            cartViewModel.deleteByIdCart(Cart(it,"","","","","",1))
        }

    }

    private fun swipeToDelete() {

        val swipeCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val selectedFavorites = cartAdapter.carts[position]
                cartViewModel.deleteByIdCart(selectedFavorites)
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