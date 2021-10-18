package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_app.R
import com.example.movies_app.databinding.ShowAllMaoviesItemBinding
import com.example.movies_app.models.ResultXX
import com.example.movies_app.util.setupPieChart

class TopRatedMovieListAdapter(private var mOnSeeMoreTopRatedMoviesListener: OnSeeMoreTopRatedMoviesListener) :
    PagingDataAdapter<ResultXX, TopRatedMovieListAdapter.TopRatedMovieListViewHolder>(
        TopRatedMovieDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMovieListViewHolder {
        val binding: ShowAllMaoviesItemBinding = ShowAllMaoviesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopRatedMovieListViewHolder(binding)

    }


    override fun onBindViewHolder(holder: TopRatedMovieListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, mOnSeeMoreTopRatedMoviesListener) }
    }

    inner class TopRatedMovieListViewHolder(
        val binding: ShowAllMaoviesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultXX, clickListener: OnSeeMoreTopRatedMoviesListener) {
            result.let {
                binding.showAllMoviesMovieName.text = result.title
                setupPieChart(itemView.context, binding.movieRatePieChart, result.vote_average * 10)
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500/${result.poster_path}")
                    .placeholder(R.drawable.loding_animation)
                    .error(R.drawable.poster_placeholder)
                    .into(binding.showAllMoviesPoster)

                binding.showAllUpComingMoviesConstraintLayout.setOnClickListener {
                    clickListener.seeMoreSelectedTopRatedMovieId(result.id)
                }


            }
        }
    }

    interface OnSeeMoreTopRatedMoviesListener {
        fun seeMoreSelectedTopRatedMovieId(TopRatedMovieId: Int)
    }
}

class TopRatedMovieDiffCallBack : DiffUtil.ItemCallback<ResultXX>() {
    override fun areItemsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean {
        return oldItem == newItem
    }

}

