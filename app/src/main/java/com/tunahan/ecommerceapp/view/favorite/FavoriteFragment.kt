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
import com.tunahan.ecommerceapp.util.SwipeToDeleteCallback
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteAdapter by lazy { FavoriteAdapter(requireContext()) }

    private lateinit var mHomeViewModel: HomeViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        mHomeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

       // favoriteAdapter = FavoriteAdapter(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mHomeViewModel.readAllFavorite.observe(viewLifecycleOwner, Observer {
            favoriteAdapter.setData(it)
            binding.recyclerView.adapter = favoriteAdapter
        })

        swipeToDelete()

    }

    private fun swipeToDelete() {

        val swipeCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val selectedFavorites = favoriteAdapter.favorites[position]
                mHomeViewModel.deleteFavorite(selectedFavorites)
                favoriteAdapter.notifyItemRemoved(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}