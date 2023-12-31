package com.tunahan.ecommerceapp.presentation.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunahan.ecommerceapp.databinding.AdminRowBinding
import com.tunahan.ecommerceapp.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

class AdminAdapter(
    private val productList: ArrayList<Product>,
    private val context: Context
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    class AdminViewHolder(val binding: AdminRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = AdminRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val currentList = productList[position]

        val currentUuid = currentList.documentUuid.toString()
        holder.binding.uuidText.text = currentUuid
        holder.binding.bookNameText.text = currentList.bookName
        holder.binding.priceText.text = "${currentList.price} ₺"
        Glide.with(context)
            .load(currentList.downloadUrl)
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(holder.binding.recyclerIV)

        holder.binding.cardView.setOnClickListener {
            val action = AdminFragmentDirections.actionAdminFragmentToAdminUpdateFragment(
                productList.size,
                position,
                currentUuid
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun getSize(): Int {
        return productList.size
    }

}