package com.tunahan.ecommerceapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunahan.ecommerceapp.databinding.CartRowBinding
import com.tunahan.ecommerceapp.model.Cart

class CartAdapter(private val context: Context) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(val binding: CartRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)


    var carts: List<Cart>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = CartRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentCart = carts[position]

        holder.binding.bookIdCart.text = currentCart.bookId
        holder.binding.bookNameCart.text = currentCart.bookName
        holder.binding.writerCart.text = currentCart.writer
        holder.binding.priceCart.text = "${ currentCart.price } ₺"

        Glide.with(context).load(currentCart.imageUrl).into(holder.binding.cartIV)
    }

    fun setData(cartList: List<Cart>) {
        carts = cartList
        notifyItemRangeInserted(0,cartList.size)
    }

}