package com.ezgikara.gathereality.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ezgikara.gathereality.common.gone
import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.common.visible
import com.ezgikara.gathereality.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding (ActivityMainBinding::inflate)

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(com.ezgikara.gathereality.R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.buttomNavbar, navController)

        navHostFragment.navController.addOnDestinationChangedListener{ _, destination, _ ->

            when (destination.id) {
                com.ezgikara.gathereality.R.id.splashFragment,
                com.ezgikara.gathereality.R.id.signInFragment,
                com.ezgikara.gathereality.R.id.signUpFragment,
                com.ezgikara.gathereality.R.id.successFragment

                -> {
                    binding.buttomNavbar.gone()
                }

                else -> {
                    binding.buttomNavbar.visible()
                }
            }
        }
    }
}