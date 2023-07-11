package com.tunahan.ecommerceapp.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tunahan.ecommerceapp.adapter.AdminAdapter
import com.tunahan.ecommerceapp.adapter.BookCategoryAdapter
import com.tunahan.ecommerceapp.databinding.FragmentAdminUpdateBinding
import com.tunahan.ecommerceapp.model.Document
import com.tunahan.ecommerceapp.model.Product
import com.tunahan.ecommerceapp.viewmodel.HomeViewModel


class AdminUpdateFragment : Fragment() {


    private var _binding: FragmentAdminUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var mHomeViewModel: HomeViewModel
    private val mList = ArrayList<String>()
    private lateinit var bookCategoryAdapter: BookCategoryAdapter
    private var productList = ArrayList<Product>()
    private val args by navArgs<AdminUpdateFragmentArgs>()
    private var documentId = ""
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUpdateBinding.inflate(inflater, container, false)
        mHomeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val positionToSize = args.listSize - (args.position + 1)
        mHomeViewModel.readAllData.observe(viewLifecycleOwner, Observer { document ->
            documentId = document[positionToSize].documentID.toString()
            val docId = document[positionToSize].documentID.toString()
            readData(docId)
        })


        binding.updateBookButton.setOnClickListener {
            updateData(documentId)
        }

        binding.deleteBookButton.setOnClickListener {
            println(documentId)
        }


        listAdd()
        bookCategoryAdapter =
            BookCategoryAdapter(mList, object : BookCategoryAdapter.OnClickListener {
                override fun onClick(item: String) {
                    Toast.makeText(requireContext(), item, Toast.LENGTH_LONG).show()
                }
            })
        val rv = binding.categoryRV
        rv.adapter = bookCategoryAdapter
    }

    private fun readData(docId: String) {
        val docRef = db.collection("books").document(
            docId
        )

        docRef.get().addOnSuccessListener {

            if (it != null) {
                productList.clear()
                val document = it.data

                val url = document?.get("downloadUrl") as String?
                val bookName = document?.get("bookName") as String?
                val price = document?.get("price") as String?
                val writer = document?.get("writer") as String?
                val publisher = document?.get("publisher") as String?
                val pageCount = document?.get("pageCount") as String?
                val publicationYear = document?.get("publicationYear") as String?
                val language = document?.get("language") as String?

                binding.bookNameText.setText(bookName)
                binding.priceText.setText(price)
                binding.writerText.setText(writer)
                binding.publisherText.setText(publisher)
                binding.pageCountText.setText(pageCount)
                binding.publicationYearText.setText(publicationYear)
                binding.languageText.setText(language)
                Glide.with(requireContext()).load(url).into(binding.addImage)

            }

        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateData(docId: String) {
        //  val dowloadUrl = uri.toString()
      //  val date = Timestamp.now()
        val bookName = binding.bookNameText.text.toString()
        val price = binding.priceText.text.toString()
        val writer = binding.writerText.text.toString()
        val publisher = binding.publisherText.text.toString()
        val pageCount = binding.pageCountText.text.toString()
        val publicationYear = binding.publicationYearText.text.toString()
        val language = binding.languageText.text.toString()

        val addMap = hashMapOf<String, Any>()

      //  addMap["date"] = date
        addMap["bookName"] = bookName
        addMap["price"] = price
        addMap["writer"] = writer
        addMap["publisher"] = publisher
        addMap["pageCount"] = pageCount
        addMap["publicationYear"] = publicationYear
        addMap["language"] = language
        //addMap["category"] = category
        //  addMap["downloadUrl"] = dowloadUrl


        db.collection("books").document(docId).update(addMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                findNavController().navigate(AdminUpdateFragmentDirections.actionAdminUpdateFragmentToAdminFragment())
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun listAdd() {
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