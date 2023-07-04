package com.tunahan.ecommerceapp.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminUpdateBinding


class AdminUpdateFragment : Fragment() {


    private var _binding: FragmentAdminUpdateBinding? = null
    private val binding get() = _binding!!

    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter

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
        bookCategoryAdapter = BookCategoryAdapter(mList)
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