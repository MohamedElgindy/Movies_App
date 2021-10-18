package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_app.R
import com.example.movies_app.databinding.ShowAllMaoviesItemBinding
import com.example.movies_app.models.Result
import com.example.movies_app.util.setupPieChart

class UpComingMovieListAdapter(private var mOnSeeMoreUpComingMoviesListener: OnSeeMoreUpComingMoviesListener) :
    PagingDataAdapter<Result, UpComingMovieListAdapter.MoviePosterViewHolder>(
        UpComingMoviesDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val binding: ShowAllMaoviesItemBinding = ShowAllMaoviesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoviePosterViewHolder(binding)

    }


    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, mOnSeeMoreUpComingMoviesListener) }
    }

    inner class MoviePosterViewHolder(
        val binding: ShowAllMaoviesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result, clickListener: OnSeeMoreUpComingMoviesListener) {
            result.let {
                binding.showAllMoviesMovieName.text = result.title
                setupPieChart(itemView.context, binding.movieRatePieChart, result.vote_average * 10)
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500/${result.poster_path}")
                    .placeholder(R.drawable.loding_animation)
                    .error(R.drawable.poster_placeholder)
                    .into(binding.showAllMoviesPoster)

                binding.showAllUpComingMoviesConstraintLayout.setOnClickListener {
                    clickListener.seeMoreSelectedUpComingMovieId(result.id)
                }
            }
        }
    }

    interface OnSeeMoreUpComingMoviesListener {
        fun seeMoreSelectedUpComingMovieId(upComingMovieId: Int)
    }
}
class UpComingMoviesDiffCallBack : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }
}

