package com.tunahan.ecommerceapp.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminUpdateBinding


class AdminUpdateFragment : Fragment() {


    private var _binding: FragmentAdminUpdateBinding? = null
    private val binding get() = _binding!!

    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdd()
        bookCategoryAdapter = BookCategoryAdapter(mList,object : BookCategoryAdapter.OnClickListener{
            override fun onClick(item: String) {
                Toast.makeText(requireContext(),item, Toast.LENGTH_LONG).show()
            }

        })
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter
    }



    private fun listAdd(){
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