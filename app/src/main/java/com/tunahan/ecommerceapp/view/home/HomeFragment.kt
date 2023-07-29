package com.tunahan.ecommerceapp.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.adapter.HomeProductAdapter
import com.tunahan.ecommerceapp.databinding.FragmentHomeBinding
import com.tunahan.ecommerceapp.model.Cart
import com.tunahan.ecommerceapp.model.Favorite
import com.tunahan.ecommerceapp.model.Product
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel
import kotlin.math.log


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var count = 0
    private lateinit var homeProductAdapter: HomeProductAdapter
    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    private var productList = ArrayList<Product>()
    private var favoriteList = ArrayList<Favorite>()

    private lateinit var mHomeViewModel: HomeViewModel

    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser?.uid.toString()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        mHomeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

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

        mHomeViewModel.readAllFavorite.observe(viewLifecycleOwner, Observer {
            for (favorites in it) {
                val favList = Favorite(
                    favorites.id,
                    favorites.bookId,
                    favorites.bookName,
                    favorites.imageUrl,
                    favorites.writer,
                    favorites.publisher,
                    favorites.price
                )
                favoriteList.add(favList)
            }

        })

        homeProductAdapter.addClick = { id, name, url, writer, price ->
            val carts = Cart(0, id, name, url, writer, price, 1)
            mHomeViewModel.addCart(carts)
        }
        // readFavorite()

        if (count == 0) {
            readAllData()
            count++
        }

        binding.cardView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }
    }

    private fun readAllData() {
        db.collection("books")/*.orderBy(
            "date",
            Query.Direction.DESCENDING
        )*/.addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        binding.homeProductRV.visibility = View.VISIBLE
                        binding.warningText.visibility = View.GONE
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

                            val bookUuid = document?.get("bookUuid") as String?
                            val readProduct = Product(
                                bookUuid,
                                url,
                                bookName,
                                price,
                                writer,
                                publisher,
                                pageCount,
                                publicationYear,
                                language,
                                true
                            )

                            productList.add(readProduct)

                        }

                        homeProductAdapter.notifyDataSetChanged()
                    } else {
                        binding.homeProductRV.visibility = View.GONE
                        binding.warningText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun readData(categoryFilter: String) {
        db.collection("books").whereEqualTo("category", categoryFilter)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            binding.homeProductRV.visibility = View.VISIBLE
                            binding.warningText.visibility = View.GONE
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
                                val bookUuid = document?.get("bookUuid") as String?

                                val readProduct = Product(
                                    bookUuid,
                                    url,
                                    bookName,
                                    price,
                                    writer,
                                    publisher,
                                    pageCount,
                                    publicationYear,
                                    language,
                                    true
                                )

                                productList.add(readProduct)
                            }

                            homeProductAdapter.notifyDataSetChanged()
                        } else {
                            binding.homeProductRV.visibility = View.GONE
                            binding.warningText.visibility = View.VISIBLE
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