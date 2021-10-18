package com.example.movies_app.ui.fragments.TopRatedMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class TopRatedMoviesViewModel : ViewModel() {

    val flow = Pager(
        PagingConfig(25)
    ) {
        TopRatedMoviesPagingSource()
    }.flow
        .cachedIn(viewModelScope)
}