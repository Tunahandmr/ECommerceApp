package com.tunahan.ecommerceapp.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminAddBinding


class AdminAddFragment : Fragment() {

    private var _binding: FragmentAdminAddBinding? = null
    private val binding get() = _binding!!

    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    var category = ""

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddBinding.inflate(inflater, container, false)
        bookCategoryAdapter = BookCategoryAdapter(mList,object : BookCategoryAdapter.OnClickListener{
            override fun onClick(item: String) {
                category=item
            }

        })
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdd()


        binding.saveBookButton.setOnClickListener {
            val date = Timestamp.now()
            val bookName = binding.bookNameText.text.toString()
            val price = binding.priceText.text.toString()
            val writer = binding.writerText.text.toString()
            val publisher = binding.publisherText.text.toString()
            val pageCount = binding.pageCountText.text.toString()
            val publicationYear = binding.publicationYearText.text.toString()
            val language = binding.languageText.text.toString()




            val addMap = hashMapOf<String,Any>()

            addMap["date"] = date
            addMap["bookName"] = bookName
            addMap["price"] = price
            addMap["writer"] = writer
            addMap["publisher"] = publisher
            addMap["pageCount"] = pageCount
            addMap["publicationYear"] = publicationYear
            addMap["language"] = language
            addMap["category"] = category


            db.collection("books").add(addMap).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    findNavController().navigate(AdminAddFragmentDirections.actionAdminAddFragmentToAdminFragment())
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }


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