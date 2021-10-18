package com.example.movies_app.ui.fragments.NowPlayingMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class NowPlayingMoviesViewModel : ViewModel() {

    val flow = Pager(
        PagingConfig(25)
    ) {
        NowPlayingMoviesPagingSource()
    }.flow
        .cachedIn(viewModelScope)

}