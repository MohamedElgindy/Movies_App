package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies_app.R
import com.example.movies_app.databinding.ShowAllMaoviesItemBinding
import com.example.movies_app.models.ResultX
import com.example.movies_app.util.setupPieChart

class NowPlayingMovieListAdapter(private var mOnSeeNowPlayingMoviesListener: OnSeeMoreNowPlayingMoviesListener) :
    PagingDataAdapter<ResultX, NowPlayingMovieListAdapter.SeeMoreNowPlayingMoviesViewHolder>(
        NowPlayingMovieDiffCallBack()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeeMoreNowPlayingMoviesViewHolder {
        val binding: ShowAllMaoviesItemBinding = ShowAllMaoviesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeeMoreNowPlayingMoviesViewHolder(binding)

    }


    override fun onBindViewHolder(holder: SeeMoreNowPlayingMoviesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, mOnSeeNowPlayingMoviesListener) }
    }

    inner class SeeMoreNowPlayingMoviesViewHolder(
        //itemView: View,
        val binding: ShowAllMaoviesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultX, clickListener: OnSeeMoreNowPlayingMoviesListener) {
            result.let {
                binding.showAllMoviesMovieName.text = result.title
                setupPieChart(itemView.context, binding.movieRatePieChart, result.vote_average * 10)
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500/${result.poster_path}")
                    .placeholder(R.drawable.loding_animation)
                    .error(R.drawable.poster_placeholder)
                    .into(binding.showAllMoviesPoster)

                binding.showAllUpComingMoviesConstraintLayout.setOnClickListener {
                    clickListener.seeMoreSelectedNowPlayingMovieId(result.id)
                }


            }
        }
    }

    interface OnSeeMoreNowPlayingMoviesListener {
        fun seeMoreSelectedNowPlayingMovieId(nowPlayingMovieId: Int)
    }


}


class NowPlayingMovieDiffCallBack : DiffUtil.ItemCallback<ResultX>() {
    override fun areItemsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
        return oldItem == newItem
    }

}

