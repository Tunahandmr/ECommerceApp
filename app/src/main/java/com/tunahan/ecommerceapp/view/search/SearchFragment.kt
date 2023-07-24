package com.tunahan.ecommerceapp.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.adapter.AdminAdapter
import com.tunahan.ecommerceapp.databinding.FragmentSearchBinding
import com.tunahan.ecommerceapp.model.Product
import java.util.Locale

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore
    private var productList = ArrayList<Product>()
    private lateinit var adminAdapter: AdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.searchRV.layoutManager = layoutManager
        adminAdapter = AdminAdapter(productList,requireContext())
        binding.searchRV.adapter = adminAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.requestFocus()

        binding.backButton.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToHomeFragment())
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (!query.isNullOrEmpty()) {
                    // Firestore sorgusu ile verileri filtreleme
                    db.collection("books").whereEqualTo("bookName", query).addSnapshotListener { value, error ->
                        if (error!=null){
                            Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                        }else{
                            if (value!=null){
                                if (!value.isEmpty){
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
                                            false
                                        )

                                        productList.add(readProduct)
                                    }

                                    adminAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.lowercase(Locale.getDefault())?.trim()

                if (!newText.isNullOrEmpty()) {
                    // Firestore sorgusu ile verileri filtreleme
                    db.collection("books").whereEqualTo("bookName", newText).addSnapshotListener { value, error ->
                        if (error!=null){
                            Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                        }else{
                            if (value!=null){
                                if (!value.isEmpty){
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
                                            false
                                        )

                                        productList.add(readProduct)
                                    }

                                    adminAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }

                }

                return false
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}