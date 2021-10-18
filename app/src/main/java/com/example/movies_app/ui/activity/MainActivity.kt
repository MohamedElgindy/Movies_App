package com.example.movies_app.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.ekhtebereducationapp.ekhteber.Utilities.ConnectToInternet
import com.example.movies_app.R
import com.example.movies_app.databinding.ActivityMainBinding
import com.example.movies_app.ui.fragments.MoviesDetails.MovieDetailsViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var connectToInternet: ConnectToInternet
    lateinit var movieDetailsViewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        movieDetailsViewModel =
            ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        connectToInternet = ConnectToInternet(this)


        navController = navHostFragment!!.navController
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupActionBarWithNavController(navController)

        connectToInternet.observe(this, {
            if (it)
                subscribeUi()
            else
                showNoInternet()
        })

        binding.searchIcon.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

    }

    private fun subscribeUi() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.mainConstraintLayout)

    }

    private fun showNoInternet() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.noInternetLayoutMainActivity.noInternetLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp()

    }

    fun hideSearchIcon() {
        binding.searchIcon.visibility = View.GONE
    }

    fun showSearchIcon() {
        binding.searchIcon.visibility = View.VISIBLE
    }
}