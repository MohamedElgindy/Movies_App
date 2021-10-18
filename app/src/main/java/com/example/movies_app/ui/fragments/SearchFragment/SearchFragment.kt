package com.example.movies_app.ui.fragments.SearchFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.adapters.SearchMoviesAdapter
import com.example.movies_app.databinding.SearchFragmentBinding
import com.example.movies_app.ui.activity.MainActivity
import com.example.movies_app.util.State


class SearchFragment : Fragment(), SearchMoviesAdapter.OnSearchMoviesListener {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchMoviesAdapter: SearchMoviesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        setupViews()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchViewModel.getSearchResult(it) }
                getSearchMoviesResult()
                return false
            }
        })

        return binding.root
    }

    private fun setupViews() {
        // pagination
        binding.searchMoviesRV.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchMoviesAdapter = SearchMoviesAdapter(this)
        binding.searchMoviesRV.adapter = searchMoviesAdapter
    }

    override fun selectedSearchMovie(MovieId: Int) {
        (activity as MainActivity?)!!.movieDetailsViewModel.getMovieDetails(MovieId)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_searchFragment_to_movieDetailsFragment)
    }

    private fun getSearchMoviesResult() {
        searchViewModel.searchMovieList.observe(requireActivity(), { state ->
            when (state) {
                is State.Loading ->
                    Log.e("Test ", " Loading")
                is State.Success -> {
                    searchMoviesAdapter.differ.submitList(state.data.body()?.results ?: emptyList())
                }
                is State.Failed -> {
                    Log.e("TestFailed", state.message)
                }


            }

        })

    }

    override fun onStop() {
        super.onStop()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        val activity = activity as MainActivity

        val view = activity.currentFocus

        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

}