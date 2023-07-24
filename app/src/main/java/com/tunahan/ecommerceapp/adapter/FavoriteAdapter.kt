package com.tunahan.ecommerceapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunahan.ecommerceapp.databinding.FavoriteRowBinding
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.view.favorite.FavoriteFragmentDirections
import com.tunahan.ecommerceapp.view.home.HomeFragmentDirections

class FavoriteAdapter(
    private val context: Context
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(val binding: FavoriteRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var favorites: List<Favorite>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = FavoriteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentFavorite = favorites[position]

        holder.binding.favoriteUuidText.text = currentFavorite.bookId
        holder.binding.favoriteBookName.text = currentFavorite.bookName
        holder.binding.favoriteWriter.text = currentFavorite.writer
        holder.binding.favoritePublisher.text = currentFavorite.publisher
        holder.binding.favoritePrice.text = "${currentFavorite.price} ₺"

        Glide.with(context).load(currentFavorite.imageUrl).skipMemoryCache(true).into(holder.binding.favoriteImageView)

        holder.itemView.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToHomeDetailsFragment(currentFavorite.bookId,2)
            Navigation.findNavController(it).navigate(action)
        }

    }


    fun setData(favorite:ArrayList<Favorite>){
        favorites=favorite
        notifyDataSetChanged()
    }


}