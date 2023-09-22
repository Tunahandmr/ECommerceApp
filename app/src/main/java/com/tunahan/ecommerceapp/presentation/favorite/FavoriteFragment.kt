package com.tunahan.ecommerceapp.presentation.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tunahan.ecommerceapp.databinding.FragmentFavoriteBinding
import com.tunahan.ecommerceapp.common.SwipeToDeleteCallback
import com.tunahan.ecommerceapp.presentation.favorite.components.FavoriteAdapter
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteAdapter by lazy { FavoriteAdapter(requireContext()) }

    private lateinit var favoriteViewModel: FavoriteViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)


        favoriteViewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
       // favoriteAdapter = FavoriteAdapter(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  mHomeViewModel.readAllFavorite.observe(viewLifecycleOwner, Observer {
            favoriteAdapter.setData(it)
            binding.recyclerView.adapter = favoriteAdapter
        })*/

       /* favoriteViewModel.readAllFavorite.observe(viewLifecycleOwner, Observer {
            favoriteAdapter.setData(it)
            binding.recyclerView.adapter = favoriteAdapter
        })*/

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.readAllFavorite.collect{
                    favoriteAdapter.setData(it.favorites)
                    binding.recyclerView.adapter = favoriteAdapter
                }
            }
        }


        favoriteIsEmpty()

        swipeToDelete()

    }

    private fun swipeToDelete() {

        val swipeCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val selectedFavorites = favoriteAdapter.favorites[position]
                favoriteViewModel.deleteByIdFavorite(selectedFavorites)
               // mHomeViewModel.deleteFavorite(selectedFavorites)
                favoriteAdapter.notifyItemRemoved(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun favoriteIsEmpty(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.favoriteIsEmpty.collect{
                    it.let {
                        if (it.isLoading){
                            binding.noFavoritesIV.visibility = View.VISIBLE
                            binding.noFavoritesTV.visibility = View.VISIBLE
                        }else{
                            binding.noFavoritesIV.visibility = View.GONE
                            binding.noFavoritesTV.visibility = View.GONE
                        }
                    }

                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}