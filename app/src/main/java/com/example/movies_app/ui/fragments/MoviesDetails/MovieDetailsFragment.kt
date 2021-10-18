package com.example.movies_app.ui.fragments.MoviesDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_app.R
import com.example.movies_app.adapters.CastAdapter
import com.example.movies_app.adapters.MovieRecommendationAdapter
import com.example.movies_app.databinding.FragmentMovieDetailsBinding
import com.example.movies_app.ui.fragments.ActorDetails.ActorDetailsViewModel
import com.example.movies_app.util.State
import com.example.movies_app.util.setupPieChart


class MovieDetailsFragment : Fragment(), CastAdapter.OnCastListener,
    MovieRecommendationAdapter.OnMovieRecommendationListener {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var actorDetailsViewModel: ActorDetailsViewModel
    private lateinit var castAdapter: CastAdapter
    private lateinit var movieRecommendationAdapter: MovieRecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        movieDetailsViewModel =
            ViewModelProvider(requireActivity()).get(MovieDetailsViewModel::class.java)

        actorDetailsViewModel =
            ViewModelProvider(requireActivity()).get(ActorDetailsViewModel::class.java)

        if (isAdded) {
            setUpUi()
            subscribeUi()
        }
        return binding.root
    }


    private fun showLoader() {
        binding.viewFlipperMovieDetails.displayedChild =
            binding.viewFlipperMovieDetails.indexOfChild(binding.appLoadingLayoutMovieDetails.appLoadingLayout)
    }

    private fun showView() {
        binding.viewFlipperMovieDetails.displayedChild =
            binding.viewFlipperMovieDetails.indexOfChild(binding.movieDetailsLayout)
    }


    private fun errorMessage() {
        binding.viewFlipperMovieDetails.displayedChild =
            binding.viewFlipperMovieDetails.indexOfChild(binding.errorLayoutMovieDetails.appErrorLayout)
        binding.errorLayoutMovieDetails.tryAgainBtnErrorLayout.setOnClickListener {
            showLoader()
            movieDetailsViewModel.reload()
        }
    }

    private fun setUpUi() {
        binding.castRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        castAdapter = CastAdapter(this)
        binding.castRV.adapter = castAdapter

        binding.movieRecommendationRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        movieRecommendationAdapter = MovieRecommendationAdapter(this)
        binding.movieRecommendationRV.adapter = movieRecommendationAdapter

    }

    private fun subscribeUi() {
        movieDetailsViewModel.movieDetails.observe(requireActivity(), { state ->
            when (state) {
                is State.Loading ->
                    showLoader()

                is State.Success -> {
                    val body = state.data.body()
                    binding.movieDetailsTitle.text = body?.title ?: "unKnown"
                    binding.MovieDetailsReleaseDate.text = body?.release_date ?: "unKnown"
                    binding.MovieDetailsRunTime.text = "${body?.runtime ?: 0} m"
                    binding.MovieDetailsOverView.text = body?.overview ?: "unKnown"
                    binding.MovieDetailsStatus.text = body?.status ?: "unKnown"
                    body?.vote_average?.times(10)?.let {
                        setupPieChart(
                            binding.root.context,
                            binding.MovieDetailsPieChart,
                            it
                        )
                        showView()
                    }

                    val separatedGenresNumber = body?.genres?.joinToString { it.name }
                    binding.movieDetailsGenres.text = separatedGenresNumber
                    Glide.with(binding.root)
                        .load("https://image.tmdb.org/t/p/w500/${body?.poster_path}")
                        .error(R.drawable.poster_placeholder)
                        .placeholder(R.drawable.loding_animation)
                        .override(400, 800)
                        .into(binding.movieDetailsPoster)
                    Glide.with(binding.root)
                        .load("https://image.tmdb.org/t/p/w500/${body?.backdrop_path ?: body?.poster_path}")
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.backgroundImage)

                    binding.backgroundImage.imageAlpha = 45


                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })

        movieDetailsViewModel.movieDetailsCredit.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()
                is State.Success -> {
                    val body = state.data.body()


                    val directorsList = mutableListOf<String>()
                    val storyWritersList = mutableListOf<String>()
                    val screenPlayersList = mutableListOf<String>()
                    for (i in body?.crew?.indices!!) {
                        when (body.crew[i].job) {
                            "Director" -> directorsList.add(body.crew[i].name)
                            "Story" -> storyWritersList.add(body.crew[i].name)
                            "Screenplay" -> screenPlayersList.add(body.crew[i].name)//binding.MovieDetailsScreenPlayName.text =

                        }
                    }
                    val directors = directorsList.joinToString { it }
                    val storyWriter = storyWritersList.joinToString { it }
                    val screenPlayer = screenPlayersList.joinToString { it }



                    if (directors.isNotEmpty())
                        binding.MovieDetailsDirectorName.text = directors
                    if (storyWriter.isNotEmpty())
                        binding.MovieDetailsStoryName.text = storyWriter
                    if (screenPlayer.isNotEmpty())
                        binding.MovieDetailsScreenPlayName.text = screenPlayer

                    castAdapter.differ.submitList(state.data.body()?.cast)
                    showView()
                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })

        movieDetailsViewModel.movieRecommendation.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()
                is State.Success -> {
                    Log.e("test ", state.data.body()?.results?.size.toString())
                    movieRecommendationAdapter.differ.submitList(state.data.body()?.results)
                    showView()
                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })
    }

    override fun selectedActor(ActorId: Int) {
        actorDetailsViewModel.getActorDetails(ActorId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_movieDetailsFragment_to_actorDetailsFragment)

    }

    override fun selectedMovie(MovieId: Int) {
        movieDetailsViewModel.getMovieDetails(movieId = MovieId)
    }


}