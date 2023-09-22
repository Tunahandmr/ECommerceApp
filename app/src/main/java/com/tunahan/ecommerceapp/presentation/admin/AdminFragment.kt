package com.tunahan.ecommerceapp.presentation.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.databinding.FragmentAdminBinding
import com.tunahan.ecommerceapp.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore
    private lateinit var adminAdapter: AdminAdapter
    private var productList = ArrayList<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.adminPageRecyclerview.layoutManager = layoutManager
        adminAdapter = AdminAdapter(productList,requireContext())
        binding.adminPageRecyclerview.adapter = adminAdapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readData()

        binding.adminAddFAB.setOnClickListener {
            findNavController().navigate(AdminFragmentDirections.actionAdminFragmentToAdminAddFragment())
        }
    }

    private fun readData() {
        db.collection("books").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}