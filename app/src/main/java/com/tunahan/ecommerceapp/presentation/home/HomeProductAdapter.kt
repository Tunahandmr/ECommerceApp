package com.tunahan.ecommerceapp.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tunahan.ecommerceapp.databinding.HomeProductRowBinding
import com.tunahan.ecommerceapp.domain.model.Product

class HomeProductAdapter(
    private val productList: ArrayList<Product>,
    private val context: Context,
) : RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: HomeProductRowBinding) :
        RecyclerView.ViewHolder(binding.root)


    var addClick: (String,String,String,String,String) -> Unit = {id,name,url,writer,price->

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = HomeProductRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProductList = productList[position]

        val currentUuid = currentProductList.documentUuid.toString()
        holder.binding.uuidText.text = currentUuid

        holder.binding.productNameTV.text = currentProductList.bookName
        holder.binding.productPriceTV.text = "${currentProductList.price} ₺"

        Glide.with(context)
            .load(currentProductList.downloadUrl)
            .transform(CenterInside(), RoundedCorners(24))
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(holder.binding.homeProductIV)

        holder.binding.addCartButton.setOnClickListener {
            addClick(currentUuid,currentProductList.bookName.toString(),currentProductList.downloadUrl.toString(),
                currentProductList.writer.toString(),currentProductList.price.toString())
        }

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToHomeDetailsFragment(currentUuid,1)
            Navigation.findNavController(it).navigate(action)
        }
    }


}