package com.tunahan.ecommerceapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.CategoryRowBinding
import kotlinx.android.synthetic.main.category_row.view.*

class BookCategoryAdapter(private val categoryList: ArrayList<String>,
private val onClickEvent:OnClickListener) :
    RecyclerView.Adapter<BookCategoryAdapter.MyViewHolder>() {

    private var selectItem: Int? = null

    interface OnClickListener{
        fun onClick(item:String)
    }

     class MyViewHolder(binding:CategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = CategoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.itemView.categoryText.text = currentItem

        holder.itemView.setOnClickListener {
            selectItem = position
            onClickEvent.onClick(currentItem)
            notifyDataSetChanged()
        }

        if (selectItem == position) {
            holder.itemView.categoryCard.setCardBackgroundColor(Color.parseColor("#0D47A1"))
            holder.itemView.categoryText.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.categoryCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.itemView.categoryText.setTextColor(Color.parseColor("#000000"))
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setData(newPosition:Int){
        selectItem=newPosition
        notifyDataSetChanged()
    }
}