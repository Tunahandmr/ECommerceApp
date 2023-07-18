package com.tunahan.ecommerceapp.view.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.adapter.FavoriteAdapter
import com.tunahan.ecommerceapp.adapter.HomeProductAdapter
import com.tunahan.ecommerceapp.databinding.FragmentFavoriteBinding
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var favoriteList = ArrayList<Favorite>()
    private lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var mHomeViewModel: HomeViewModel

    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser?.uid.toString()

    private val swipeCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt = favoriteAdapter.favorites[layoutPosition]
            mHomeViewModel.deleteNote(selectedArt)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        mHomeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        favoriteAdapter = FavoriteAdapter(requireContext())
        binding.recyclerView.adapter = favoriteAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mHomeViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            for (favorites in it){
                val favList = Favorite(favorites.id,favorites.bookId,favorites.bookName,favorites.imageUrl,favorites.writer,
                favorites.publisher,favorites.price)
                favoriteList.add(favList)
            }

        })

        favoriteAdapter.setData(favoriteList)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerView)

     //   getFavorites(currentUser)

    }

    fun getFavorites(userId: String) {
        val favoritesCollection = db.collection("favorites")

        favoritesCollection.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val productId = document.getString("userId")
                    val productName = document.getString("productName")
                    // Diğer alanları alın
                 //   val fav = Favorite(0,productName!!,false)
                    //favoriteList.add(fav)
                }
            }
            .addOnFailureListener { e ->
                println("Favori ürünleri alırken hata oluştu: $e")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}