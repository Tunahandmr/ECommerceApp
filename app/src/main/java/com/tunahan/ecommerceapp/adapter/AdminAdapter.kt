package com.tunahan.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.model.Product
import kotlinx.android.synthetic.main.recycler_admin_row.view.bookNameText
import kotlinx.android.synthetic.main.recycler_admin_row.view.priceText

class AdminAdapter(private val productList:ArrayList<Product>): RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    class AdminViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_admin_row,parent,false)
        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val currentList = productList[position]

        holder.itemView.bookNameText.text = currentList.bookName
        holder.itemView.priceText.text = currentList.price.toString()
    }


}