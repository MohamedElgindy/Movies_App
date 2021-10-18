package com.example.movies_app.ui.fragments.TopRatedMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies_app.R
import com.example.movies_app.adapters.FootLoadingAdapter
import com.example.movies_app.adapters.TopRatedMovieListAdapter
import com.example.movies_app.databinding.TopRatedMoviesFragmentBinding
import com.example.movies_app.ui.activity.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopRatedMoviesFragment : Fragment(),
    TopRatedMovieListAdapter.OnSeeMoreTopRatedMoviesListener {

    private var _binding: TopRatedMoviesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var topRatedViewModel: TopRatedMoviesViewModel

    // pagination
    private lateinit var topRatedMoviesListAdapter: TopRatedMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TopRatedMoviesFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        topRatedViewModel =
            ViewModelProvider(requireActivity()).get(TopRatedMoviesViewModel::class.java)

        showLoader()
        setupViews()
        loadUpComingMovies()
        return binding.root

    }

    private fun setupViews() {
        // pagination
        binding.moreTopRatedMovies.layoutManager = GridLayoutManager(context, 2)
        topRatedMoviesListAdapter = TopRatedMovieListAdapter(this)
        binding.moreTopRatedMovies.adapter = topRatedMoviesListAdapter
        binding.moreTopRatedMovies.adapter = topRatedMoviesListAdapter.withLoadStateHeaderAndFooter(
            header = FootLoadingAdapter { topRatedMoviesListAdapter.retry() },
            footer = FootLoadingAdapter { topRatedMoviesListAdapter.retry() }
        )
    }

    private fun loadUpComingMovies() {
        lifecycleScope.launch {
            topRatedViewModel.flow.collectLatest { pagingData ->
                topRatedMoviesListAdapter.submitData(pagingData)
            }
        }
        showView()
    }

    private fun showLoader() {
        binding.TopRatedMoviesViewFlipper.displayedChild =
            binding.TopRatedMoviesViewFlipper.indexOfChild(binding.appLoadingLayoutTopRatedMovies.appLoadingLayout)
    }

    private fun showView() {
        binding.TopRatedMoviesViewFlipper.displayedChild =
            binding.TopRatedMoviesViewFlipper.indexOfChild(binding.TopRatedMoviesLayout)
    }

    override fun seeMoreSelectedTopRatedMovieId(TopRatedMovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(TopRatedMovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_topRatedMoviesFragment_to_movieDetailsFragment)
    }

}