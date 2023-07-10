package com.tunahan.ecommerceapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.tunahan.ecommerceapp.databinding.HomeProductRowBinding
import com.tunahan.ecommerceapp.model.Product

class HomeProductAdapter(
    private val productList: ArrayList<Product>,
    private val context: Context
) : RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder>() {




    class ProductViewHolder(val binding: HomeProductRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = HomeProductRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProductList = productList[position]

        holder.binding.productNameTV.text = currentProductList.bookName
        holder.binding.productPriceTV.text = "${currentProductList.price} tl"
        // Picasso.get().load(currentList.downloadUrl).into(holder.binding.homeProductIV)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
        Glide.with(context)
            .load(currentProductList.downloadUrl)
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(holder.binding.homeProductIV)
    }

}