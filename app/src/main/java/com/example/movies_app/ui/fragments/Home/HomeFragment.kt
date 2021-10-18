package com.example.movies_app.ui.fragments.Home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.movies_app.R
import com.example.movies_app.adapters.NowPlayingMoviesAdapter
import com.example.movies_app.adapters.PopularMovieSliderAdapter
import com.example.movies_app.adapters.TopRatedMoviesAdapter
import com.example.movies_app.adapters.UpComingMoviesAdapter
import com.example.movies_app.databinding.FragmentHomeBinding
import com.example.movies_app.models.MoviesModel
import com.example.movies_app.ui.activity.MainActivity
import com.example.movies_app.util.State


class HomeFragment : Fragment(), UpComingMoviesAdapter.OnUpComingMoviesListener,
    NowPlayingMoviesAdapter.OnNowPlayingMovieListener,
    TopRatedMoviesAdapter.OnUpComingMoviesListener,
    PopularMovieSliderAdapter.OnPopularMoviesListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sliderHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var homeViewModel: HomeViewModel
    private val sliderItems: MutableList<MoviesModel> = ArrayList()
    private lateinit var upComingMoviesAdapter: UpComingMoviesAdapter
    private lateinit var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter
    private lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        setupViews()
        subscribeUi()
        binding.moreUpComingMoviesHome.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_homeFragment_to_upComingMoviesFragment)

        }
        binding.moreNowPlayingMoviesHome.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_homeFragment_to_nowPlayingMoviesFragment)

        }

        binding.moreTopRatedMoviesHome.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_homeFragment_to_topRatedMoviesFragment)

        }

        binding.viewPagerImageSlider.clipToPadding = false
        binding.viewPagerImageSlider.clipChildren = false
        binding.viewPagerImageSlider.offscreenPageLimit = 3
        binding.viewPagerImageSlider.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f

        }

        binding.viewPagerImageSlider.setPageTransformer(compositePageTransformer)

        binding.viewPagerImageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        })
        return binding.root
    }

    private fun setupViews() {

        binding.upComingMoviesRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        upComingMoviesAdapter = UpComingMoviesAdapter(this)
        binding.upComingMoviesRV.adapter = upComingMoviesAdapter

        binding.topRatedMoviesRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        topRatedMoviesAdapter = TopRatedMoviesAdapter(this)
        binding.topRatedMoviesRV.adapter = topRatedMoviesAdapter

        binding.nowPlayingMoviesRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter(this)
        binding.nowPlayingMoviesRV.adapter = nowPlayingMoviesAdapter
    }

    private fun showLoader() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.appLoadingLayoutHome.appLoadingLayout)
    }

    private fun showView() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.mainConstraint)
    }

    private fun errorMessage() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.errorLayoutHome.appErrorLayout)
        binding.errorLayoutHome.tryAgainBtnErrorLayout.setOnClickListener {
            showLoader()
            homeViewModel.reload()
        }
    }

    private val sliderRunnable =
        Runnable {
            binding.viewPagerImageSlider.currentItem.plus(1)
                .also { binding.viewPagerImageSlider.currentItem = it }
        }


    private fun subscribeUi() {
        homeViewModel.popularMovieList.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()
                is State.Success -> {
                    showView()

                    state.data.body()?.let { sliderItems.addAll(it.results) }
                    binding.viewPagerImageSlider.adapter =
                        PopularMovieSliderAdapter(this, sliderItems, binding.viewPagerImageSlider)

                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })
        homeViewModel.upComingMoviesList.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()

                is State.Success -> {
                    showView()
                    state.data.body()?.let { upComingMoviesAdapter.differ.submitList(it.results) }

                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })
        homeViewModel.topRatedMoviesList.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()

                is State.Success -> {
                    showView()

                    state.data.body()?.let { topRatedMoviesAdapter.differ.submitList(it.results) }
                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })
        homeViewModel.nowPlayingMovieList.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()

                is State.Success -> {
                    showView()
                    state.data.body()?.let { nowPlayingMoviesAdapter.differ.submitList(it.results) }
                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })
    }


    override fun selectedUpComingMovieId(upComingMovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(upComingMovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_homeFragment_to_movieDetailsFragment)
    }

    override fun selectedNowPlayingMovie(MovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(MovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_homeFragment_to_movieDetailsFragment)
    }

    override fun selectedTopRatedMovie(TopRatedMovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(TopRatedMovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_homeFragment_to_movieDetailsFragment)
    }

    override fun selectedPopularMovieName(popularMovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(popularMovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_homeFragment_to_movieDetailsFragment)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)

    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity?)!!.hideSearchIcon()
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
        (activity as MainActivity?)!!.showSearchIcon()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}