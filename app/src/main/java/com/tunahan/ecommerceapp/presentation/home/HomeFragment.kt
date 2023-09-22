package com.tunahan.ecommerceapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.databinding.FragmentHomeBinding
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private var count = 0
    private val mList = ArrayList<String>()
    private var productList = ArrayList<Product>()

    private val bookCategoryAdapter by lazy { BookCategoryAdapter(mList)  }
    private val homeProductAdapter by lazy { HomeProductAdapter(productList,requireContext()) }
    private val homeViewModel by viewModels<HomeViewModel>()

    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser?.uid.toString()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listAdd()

        if (count == 0) {
            readAllData()
            count++
        }
        binding.homeCategoryRV.adapter = bookCategoryAdapter

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.homeProductRV.layoutManager = layoutManager
        binding.homeProductRV.adapter = homeProductAdapter

        bookCategoryAdapter.categoryFilter = {
            if (it == "All")
                readAllData()
            else
                readData(it)
        }

        homeProductAdapter.addClick = { id, name, url, writer, price ->
            val carts = Cart(0, id, name, url, writer, price, 1)
            homeViewModel.addCart(carts)
        }

    }


    private fun readAllData() {
        db.collection("books")/*.orderBy(
            "date",
            Query.Direction.DESCENDING
        )*/.addSnapshotListener { value, error ->
            if (error != null) {

              /*  error.localizedMessage?.let {
                    Snackbar.make(requireView(), it,Snackbar.LENGTH_LONG).setAction("Ok"){}.show()
                }*/
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
//                        binding.homeProductRV.visibility = View.VISIBLE
   //                     binding.bookEmptyIV.visibility = View.GONE
     //                   binding.bookEmptyTV.visibility = View.GONE
                        val documents = value.documents
                        productList.clear()

                        for (document in documents) {

                            val url = document.get("downloadUrl") as String?
                            val bookName = document.get("bookName") as String?
                            val price = document.get("price") as String?
                            val writer = document.get("writer") as String?
                            val explanation = document.get("explanation") as String?
                            val pageCount = document.get("pageCount") as String?

                            val bookUuid = document?.get("bookUuid") as String?
                            val readProduct = Product(
                                bookUuid,
                                url,
                                bookName,
                                price,
                                writer,
                                pageCount,
                                explanation,
                                true
                            )

                            productList.add(readProduct)

                        }

                        homeProductAdapter.notifyDataSetChanged()
                    } else {
               //         binding.homeProductRV.visibility = View.GONE
                 //       binding.bookEmptyIV.visibility = View.VISIBLE
                   //     binding.bookEmptyTV.visibility = View.VISIBLE
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
                           // binding.homeProductRV.visibility = View.VISIBLE
                          //  binding.bookEmptyIV.visibility = View.GONE
                        //    binding.bookEmptyTV.visibility = View.GONE
                            val documents = value.documents
                            productList.clear()

                            for (document in documents) {

                                val url = document.get("downloadUrl") as String?
                                val bookName = document.get("bookName") as String?
                                val price = document.get("price") as String?
                                val writer = document.get("writer") as String?
                                val pageCount = document.get("pageCount") as String?
                                val explanation = document.get("explanation") as String?
                                val bookUuid = document?.get("bookUuid") as String?

                                val readProduct = Product(
                                    bookUuid,
                                    url,
                                    bookName,
                                    price,
                                    writer,
                                    pageCount,
                                    explanation,
                                    true
                                )

                                productList.add(readProduct)
                            }

                            homeProductAdapter.notifyDataSetChanged()
                        } else {
                      //      binding.homeProductRV.visibility = View.GONE
                        //    binding.bookEmptyIV.visibility = View.VISIBLE
                          //  binding.bookEmptyTV.visibility = View.VISIBLE
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