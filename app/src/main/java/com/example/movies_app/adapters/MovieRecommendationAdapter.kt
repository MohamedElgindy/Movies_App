package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies_app.R
import com.example.movies_app.databinding.MovieRecommendationItemBinding
import com.example.movies_app.models.ResultXXX

class MovieRecommendationAdapter(private var mOnMovieRecommendationListener: OnMovieRecommendationListener) :
    RecyclerView.Adapter<MovieRecommendationAdapter.MovieRecommendationViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ResultXXX>() {
        override fun areItemsTheSame(oldItem: ResultXXX, newItem: ResultXXX): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ResultXXX, newItem: ResultXXX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class MovieRecommendationViewHolder(private val itemBinding: MovieRecommendationItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movieRecommendation: ResultXXX, clickListener: OnMovieRecommendationListener) {

            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${movieRecommendation.backdrop_path}")
                .apply(RequestOptions().override(600, 300))
                .placeholder(R.drawable.loding_animation)
                .error(R.drawable.poster_placeholder)
                .into(itemBinding.movieRecommendationPoster)
            itemBinding.movieRecommendationTitle.text = movieRecommendation.title
            itemBinding.movieRecommendationRate.text =
                "${movieRecommendation.vote_average.toInt() * 10}%"
            itemBinding.movieRecommendationLayout.setOnClickListener {
                clickListener.selectedMovie(movieRecommendation.id)
            }
        }
    }

    interface OnMovieRecommendationListener {
        fun selectedMovie(MovieId: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieRecommendationAdapter.MovieRecommendationViewHolder {
        val binding: MovieRecommendationItemBinding = MovieRecommendationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieRecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieRecommendationAdapter.MovieRecommendationViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position], mOnMovieRecommendationListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}