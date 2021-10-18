package com.example.movies_app.ui.fragments.NowPlayingMovies

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
import com.example.movies_app.adapters.NowPlayingMovieListAdapter
import com.example.movies_app.databinding.NowPlayingMoviesFragmentBinding
import com.example.movies_app.ui.activity.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NowPlayingMoviesFragment : Fragment(),
    NowPlayingMovieListAdapter.OnSeeMoreNowPlayingMoviesListener {

    private var _binding: NowPlayingMoviesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var nowPlayingMovieViewModel: NowPlayingMoviesViewModel


    // pagination
    private lateinit var nowPlayingMoviesListAdapter: NowPlayingMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NowPlayingMoviesFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        nowPlayingMovieViewModel =
            ViewModelProvider(requireActivity()).get(NowPlayingMoviesViewModel::class.java)


        showLoader()
        setupViews()
        loadUpComingMovies()
        return binding.root

    }

    private fun showLoader() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.appLoadingLayoutNowPlayingMovie.appLoadingLayout)
    }

    private fun showView() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.nowPlayingMovieLayout)
    }


    private fun setupViews() {
        // pagination
        binding.moreNowPlayingMovies.layoutManager = GridLayoutManager(context, 2)
        nowPlayingMoviesListAdapter = NowPlayingMovieListAdapter(this)
        binding.moreNowPlayingMovies.adapter = nowPlayingMoviesListAdapter
        binding.moreNowPlayingMovies.adapter =
            nowPlayingMoviesListAdapter.withLoadStateHeaderAndFooter(
                header = FootLoadingAdapter { nowPlayingMoviesListAdapter.retry() },
                footer = FootLoadingAdapter { nowPlayingMoviesListAdapter.retry() }
            )
    }

    private fun loadUpComingMovies() {
        lifecycleScope.launch {
            nowPlayingMovieViewModel.flow.collectLatest { pagingData ->
                nowPlayingMoviesListAdapter.submitData(pagingData)
            }
        }
        showView()
    }

    override fun seeMoreSelectedNowPlayingMovieId(nowPlayingMovieId: Int) {
        (activity as MainActivity?)?.movieDetailsViewModel?.getMovieDetails(nowPlayingMovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_nowPlayingMoviesFragment_to_movieDetailsFragment)

    }


}