package com.example.movies_app.ui.fragments.UpComingMovies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies_app.R
import com.example.movies_app.adapters.FootLoadingAdapter
import com.example.movies_app.adapters.UpComingMovieListAdapter
import com.example.movies_app.databinding.FragmentUpComingMoviesBinding
import com.example.movies_app.ui.activity.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpComingMoviesFragment : Fragment() ,UpComingMovieListAdapter.OnSeeMoreUpComingMoviesListener{
    private var _binding: FragmentUpComingMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var upComingViewModel: UpComingMovieViewModel

    // pagination
    private lateinit var upComingMovieListAdapter: UpComingMovieListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpComingMoviesBinding.inflate(inflater,
            container,
            false)
        upComingViewModel = ViewModelProvider(requireActivity()).get(UpComingMovieViewModel::class.java)
        showLoader()
        setupViews()
        loadUpComingMovies()
        return binding.root
    }

    private fun setupViews() {
        // pagination
        binding.moreUpComingMoviesRV.layoutManager = GridLayoutManager(context,2)
        upComingMovieListAdapter = UpComingMovieListAdapter(this)
        binding.moreUpComingMoviesRV.adapter = upComingMovieListAdapter
        binding.moreUpComingMoviesRV.adapter = upComingMovieListAdapter.withLoadStateHeaderAndFooter(
            header = FootLoadingAdapter { upComingMovieListAdapter.retry() },
            footer = FootLoadingAdapter { upComingMovieListAdapter.retry() }
        )
    }

    private fun loadUpComingMovies() {
        lifecycleScope.launch {
            upComingViewModel.flow.collectLatest { pagingData ->
                upComingMovieListAdapter.submitData(pagingData)
            }
        }
        showView()
    }
    private fun showLoader() {
        binding.UpComingMoviesViewFlipper.displayedChild =
            binding.UpComingMoviesViewFlipper.indexOfChild(binding.appLoadingLayoutUpComingMovies.appLoadingLayout)
    }

    private fun showView() {
        binding.UpComingMoviesViewFlipper.displayedChild =
            binding.UpComingMoviesViewFlipper.indexOfChild(binding.UpComingMovieLayout)
    }


    override fun seeMoreSelectedUpComingMovieId(upComingMovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(upComingMovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_upComingMoviesFragment_to_movieDetailsFragment)
    }


}