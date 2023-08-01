package com.tunahan.ecommerceapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.databinding.FragmentHomeBinding
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import com.tunahan.ecommerceapp.domain.model.Product
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel


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

        if (count == 0) {
            readAllData()
            count++
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     //   currentTheme()

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

    }


    private fun currentTheme(){
        val sharedPref = activity?.getSharedPreferences("night",0)
        val nightValue = sharedPref?.getBoolean("night",false)

        if (nightValue==false){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun readAllData() {
        db.collection("books")/*.orderBy(
            "date",
            Query.Direction.DESCENDING
        )*/.addSnapshotListener { value, error ->
            if (error != null) {

                error.localizedMessage?.let {
                    Snackbar.make(requireView(), it,Snackbar.LENGTH_LONG).setAction("Ok"){

                    }.show()
                }
               // Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
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