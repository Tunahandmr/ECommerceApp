package com.tunahan.ecommerceapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tunahan.ecommerceapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.requestFocus()

        binding.backButton.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToHomeFragment())
        }

        /*
         //searchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}