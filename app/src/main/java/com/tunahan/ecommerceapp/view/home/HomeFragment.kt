package com.tunahan.ecommerceapp.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.adapter.HomeProductAdapter
import com.tunahan.ecommerceapp.databinding.FragmentHomeBinding
import com.tunahan.ecommerceapp.model.Product
import kotlin.math.log


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeProductAdapter: HomeProductAdapter
    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    private var productList = ArrayList<Product>()

    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        bookCategoryAdapter =
            BookCategoryAdapter(mList, object : BookCategoryAdapter.OnClickListener {
                override fun onClick(item: String) {

                    if (item == "All")
                        readAllData()
                    else
                        readData(item)
                }
            })
        val rv = binding.homeCategoryRV
        rv.adapter = bookCategoryAdapter

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.homeProductRV.layoutManager = layoutManager
        homeProductAdapter = HomeProductAdapter(productList, requireContext())
        binding.homeProductRV.adapter = homeProductAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdd()

        repeat(1){
            readAllData()
        }


        binding.cardView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }
    }

    private fun readAllData() {
        db.collection("books").orderBy(
            "date",
            Query.Direction.DESCENDING
        ).addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        val documents = value.documents
                        productList.clear()
                        for (document in documents) {

                            val url = document.get("downloadUrl") as String?
                            val bookName = document.get("bookName") as String?
                            val price = document.get("price") as String?
                            val writer = document.get("writer") as String?
                            val publisher = document.get("publisher") as String?
                            val pageCount = document.get("pageCount") as String?
                            val publicationYear = document.get("publicationYear") as String?
                            val language = document.get("language") as String?

                            val readProduct = Product(
                                url,
                                bookName,
                                price,
                                writer,
                                publisher,
                                pageCount,
                                publicationYear,
                                language
                            )

                            productList.add(readProduct)
                        }

                        homeProductAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun readData(categoryFilter: String) {
        db.collection("books").whereEqualTo("category", categoryFilter)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    //Log.d("dizinhata",error.localizedMessage)
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            val documents = value.documents
                            productList.clear()
                            for (document in documents) {

                                val url = document.get("downloadUrl") as String?
                                val bookName = document.get("bookName") as String?
                                val price = document.get("price") as String?
                                val writer = document.get("writer") as String?
                                val publisher = document.get("publisher") as String?
                                val pageCount = document.get("pageCount") as String?
                                val publicationYear = document.get("publicationYear") as String?
                                val language = document.get("language") as String?

                                val readProduct = Product(
                                    url,
                                    bookName,
                                    price,
                                    writer,
                                    publisher,
                                    pageCount,
                                    publicationYear,
                                    language
                                )

                                productList.add(readProduct)
                            }

                            homeProductAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun listAdd() {
        mList.add("All")
        mList.add("Classics")
        mList.add("Fantasy")
        mList.add("Horror")
        mList.add("Self-improvement")
        mList.add("History")
        mList.add("Philosophy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}