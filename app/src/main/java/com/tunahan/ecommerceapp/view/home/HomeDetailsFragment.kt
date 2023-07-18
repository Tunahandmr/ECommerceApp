package com.tunahan.ecommerceapp.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.databinding.FragmentHomeDetailsBinding
import com.tunahan.ecommerceapp.model.Product
import com.tunahan.ecommerceapp.view.admin.AdminUpdateFragmentArgs

class HomeDetailsFragment : Fragment() {

    private var _binding: FragmentHomeDetailsBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore
    var myCategory = ""

    private val args by navArgs<HomeDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            if (args.actionCount==1){
                findNavController().navigate(HomeDetailsFragmentDirections.actionHomeDetailsFragmentToHomeFragment())
            }else{
                findNavController().navigate(HomeDetailsFragmentDirections.actionHomeDetailsFragmentToFavoriteFragment())
            }

        }

        readData(args.bookUuid)
    }


    private fun readData(documentId:String) {
        db.collection("books").document(documentId).get().addOnSuccessListener { task ->
            if (task != null) {

                val document = task.data

                val url = document?.get("downloadUrl") as String?
                val bookName = document?.get("bookName") as String?
                val price = document?.get("price") as String?
                val writer = document?.get("writer") as String?
                val publisher = document?.get("publisher") as String?
                val pageCount = document?.get("pageCount") as String?
                val publicationYear = document?.get("publicationYear") as String?
                val language = document?.get("language") as String?
                val category = document?.get("category") as String?
                val bookUuid = document?.get("bookUuid") as String?

                myCategory = category.toString()
                var myCounter: Int? = null

                when (category) {
                    "Classics" -> myCounter = 0
                    "Fantasy" -> myCounter = 1
                    "Horror" -> myCounter = 2
                    "Self-improvement" -> myCounter = 3
                    "History" -> myCounter = 4
                    "Philosophy" -> myCounter = 5

                }

                binding.bookNameText.text = bookName
                binding.priceText.text  = "${price} $"
                binding.writerNameText.text = writer
                binding.publisherText.text = publisher
                binding.pageCountText.text = pageCount
                binding.publicationYearText.text = publicationYear
                binding.languageText.text = language
                binding.categoryText.text = category


                Glide.with(requireContext()).load(url).into(binding.bookImage)
            }
        }.addOnFailureListener { exception->
            Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}