package com.tunahan.ecommerceapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.HomeProductRowBinding
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.model.Product
import com.tunahan.ecommerceapp.view.admin.AdminFragmentDirections
import com.tunahan.ecommerceapp.view.home.HomeFragmentDirections
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.category_row.view.categoryCard
import kotlinx.android.synthetic.main.category_row.view.categoryText

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