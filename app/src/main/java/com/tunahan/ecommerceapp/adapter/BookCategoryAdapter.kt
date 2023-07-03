package com.tunahan.ecommerceapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tunahan.ecommerceapp.R
import kotlinx.android.synthetic.main.category_row.view.*

class BookCategoryAdapter(private val categoryList:ArrayList<String>): RecyclerView.Adapter<BookCategoryAdapter.MyViewHolder>() {

    private var selectItem: Int? = null


    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.itemView.categoryText.text = currentItem

        holder.itemView.setOnClickListener {
            selectItem = position
            notifyDataSetChanged()
        }

        if(selectItem==position){
            holder.itemView.categoryCard.setCardBackgroundColor(Color.parseColor("#BBDEFB"))
        }else{
            holder.itemView.categoryCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}