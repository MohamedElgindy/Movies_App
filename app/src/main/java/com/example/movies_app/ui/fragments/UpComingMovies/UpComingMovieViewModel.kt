package com.example.movies_app.ui.fragments.UpComingMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class UpComingMovieViewModel : ViewModel() {


    val flow = Pager(
        PagingConfig(25)
    ) {
        UpComingMoviesPagingSource()
    }.flow
        .cachedIn(viewModelScope)

}