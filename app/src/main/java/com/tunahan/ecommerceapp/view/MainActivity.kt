package com.tunahan.ecommerceapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.tunahan.ecommerceapp.R
import com.tunahan.ecommerceapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.searchFragment,
                R.id.updatePasswordFragment,
                R.id.forgotPasswordFragment,
                R.id.signInFragment,
                R.id.signUpFragment,
                R.id.adminAddFragment,
                R.id.adminUpdateFragment,
                R.id.adminFragment -> {
                    binding.bottomNavView.isGone = true
                }
                else -> {
                    binding.bottomNavView.isVisible = true
                }
            }
        }
    }
}