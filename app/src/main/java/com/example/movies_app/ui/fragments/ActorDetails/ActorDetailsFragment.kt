package com.example.movies_app.ui.fragments.ActorDetails

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
import com.example.movies_app.adapters.ActorDetailsAdapter
import com.example.movies_app.databinding.FragmentActorDetailsBinding
import com.example.movies_app.ui.activity.MainActivity
import com.example.movies_app.util.State

class ActorDetailsFragment : Fragment(), ActorDetailsAdapter.OnKnownForMovieListener {
    private var _binding: FragmentActorDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var actorDetailsViewModel: ActorDetailsViewModel
    private lateinit var actorDetailsAdapter: ActorDetailsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        actorDetailsViewModel =
            ViewModelProvider(requireActivity()).get(ActorDetailsViewModel::class.java)

        setUpUi()
        subscribeUi()
        return binding.root
    }

    private fun showLoader() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.appLoadingLayoutActorDetails.appLoadingLayout)
    }

    private fun showView() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.actorDetailsLayout)
    }

    private fun errorMessage() {
        binding.viewFlipper.displayedChild =
            binding.viewFlipper.indexOfChild(binding.errorLayoutActorDetails.appErrorLayout)
        binding.errorLayoutActorDetails.tryAgainBtnErrorLayout.setOnClickListener {
            showLoader()
            actorDetailsViewModel.reload()
        }
    }

    private fun setUpUi() {
        binding.knownForRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        actorDetailsAdapter = ActorDetailsAdapter(this)
        binding.knownForRV.adapter = actorDetailsAdapter
    }

    private fun subscribeUi() {
        actorDetailsViewModel.actorDetails.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()
                is State.Success -> {
                    val body = state.data.body()
                    Log.e("test", "fire")
                    binding.actorNameTV.text = body?.name ?: "unknown"
                    binding.placeOfBirthTV.text = body?.place_of_birth ?: "unknown"
                    binding.knownForTV.text = body?.known_for_department ?: "unknown"
                    binding.genderTV.text = when (body?.gender) {
                        1 -> "Female"
                        2 -> "Male"
                        else ->
                            "unKnown"
                    }
                    binding.biographyTV.text = body?.biography ?: "unknown"
                    binding.birthdayTV.text = body?.birthday ?: "unknown"
                    Glide.with(binding.root)
                        .load("https://image.tmdb.org/t/p/w500/${body?.profile_path}")
                        .placeholder(R.drawable.loding_animation)
                        .error(R.drawable.person_placeholder)
                        .override(400, 400)
                        .into(binding.actorImageView)

                    showView()
                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }


            }

        })

        actorDetailsViewModel.actorMovieCredit.observe(requireActivity(), { state ->

            when (state) {
                is State.Loading ->
                    showLoader()
                is State.Success -> {
                    actorDetailsAdapter.differ.submitList(state.data.body()?.cast ?: emptyList())
                    showView()
                }
                is State.Failed -> {
                    errorMessage()
                    Log.e("TestFailed", state.message)
                }

            }

        })

    }


    override fun selectedKnownForMovie(MovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(MovieId)

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_actorDetailsFragment_to_movieDetailsFragment)

    }
}