package com.tunahan.ecommerceapp.presentation.details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.FragmentHomeDetailsBinding
import com.tunahan.ecommerceapp.domain.model.Cart
import com.tunahan.ecommerceapp.domain.model.Favorite
import kotlinx.coroutines.launch

class HomeDetailsFragment : Fragment() {

    private var _binding: FragmentHomeDetailsBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore
    val auth = Firebase.auth
    var myCategory = ""

    private val args by navArgs<HomeDetailsFragmentArgs>()
    private var favoriteList = ArrayList<Favorite>()

    private lateinit var homeDetailsViewModel: HomeDetailsViewModel

    var url: String? = null
    var bookName: String? = null
    var price: String? = null
    var writer: String? = null
    var favoriteId: Int? = null

    private var favoriteBool: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeDetailsViewModel.readAllFavorite.collect{
                    for (favorites in it.favorites) {
                        favoriteList.add(favorites)
                        if (args.bookUuid == favorites.bookId) {
                            favoriteId = favorites.id

                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDetailsBinding.inflate(inflater, container, false)
        homeDetailsViewModel =
            ViewModelProvider(requireActivity())[HomeDetailsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        readData(args.bookUuid)




        val sharedPref = activity?.getSharedPreferences(args.bookUuid, Context.MODE_PRIVATE)
        val favoriteBoolean = sharedPref?.getBoolean("favorite", true)

        if (favoriteBoolean == false) {
            binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
        }

        binding.favoriteButton.setOnClickListener {
            val favoriteBoolean = sharedPref?.getBoolean("favorite", true)

            if (favoriteBoolean != null) {
                if (favoriteBoolean == false) {

                    println("girdi")
                    deleteFavorites()
                    with(sharedPref.edit()) {
                        this?.putBoolean("favorite", true)
                        this?.apply()
                    }

                    binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border)
                } else {
                    val favorites =
                        Favorite(
                            0,
                            args.bookUuid,
                            bookName!!,
                            url!!,
                            writer!!,
                            price!!
                        )
                    homeDetailsViewModel.addFavorite(favorites)

                    with(sharedPref.edit()) {
                        this?.putBoolean("favorite", false)
                        this?.apply()
                    }

                    binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
                }

            }
        }

        binding.backButton.setOnClickListener {
            if (args.actionCount == 1) {
                findNavController().navigate(HomeDetailsFragmentDirections.actionHomeDetailsFragmentToHomeFragment())
            } else {
                findNavController().navigate(HomeDetailsFragmentDirections.actionHomeDetailsFragmentToFavoriteFragment())
            }

        }

        binding.addToCartButton.setOnClickListener {
            addToCart()
        }

        readFavorite()



    }

    private fun readData(documentId: String) {
        db.collection("books").document(documentId).get().addOnSuccessListener { task ->
            if (task != null) {

                val document = task.data

                url = document?.get("downloadUrl") as String?
                bookName = document?.get("bookName") as String?
                price = document?.get("price") as String?
                writer = document?.get("writer") as String?
                val pageCount = document?.get("pageCount") as String?
                val explanation = document?.get("explanation") as String?
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
                binding.priceText.text = "${price} $"
                binding.writerNameText.text = writer
                binding.explanationText.text = explanation
                binding.pageCountText.text = pageCount
                binding.categoryText.text = category


                Glide.with(requireContext()).load(url).into(binding.bookImage)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun readFavorite() {
        val userId = auth.currentUser?.uid.toString()
        val bookId = args.bookUuid
        val docRef =
            db.collection("favorites").document(userId).collection("favorite").document(bookId)
        docRef.addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    val data = value.data

                    favoriteBool = data?.get("favoriteBool") as Boolean?

                }
            }
        }

    }

    private fun addToCart(){

            val carts =
                Cart(
                    0,args.bookUuid,bookName!!,url!!,writer!!,price!!,1
                )

            homeDetailsViewModel.addCart(carts)

    }

    private fun addFavorite() {

        val userId = auth.currentUser?.uid.toString()
        val bookId = args.bookUuid

        val addMap = hashMapOf<String, Any>()

        addMap["favoriteBool"] = true

        db.collection("favorites").document(userId).collection("favorite").document(bookId)
            .set(addMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Add to favorites successful!", Toast.LENGTH_LONG)
                    .show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }

    private fun updateFavorite() {

        val userId = auth.currentUser?.uid.toString()
        val bookId = args.bookUuid

        val addMap = hashMapOf<String, Any>()

        addMap["favoriteBool"] = false

        db.collection("favorites").document(userId).collection("favorite").document(bookId)
            .update(addMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Delete to favorites successful!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }


    private fun deleteFavorites() {
        val favorites =
            favoriteId?.let {
                Favorite(
                    it,
                    args.bookUuid,
                    bookName!!,
                    url!!,
                    writer!!,
                    price!!
                )
            }
        favorites?.let { homeDetailsViewModel.deleteByIdFavorite(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}