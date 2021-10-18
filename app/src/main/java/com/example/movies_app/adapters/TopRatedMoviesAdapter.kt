package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies_app.R
import com.example.movies_app.databinding.MoviePosterItemBinding
import com.example.movies_app.models.ResultXX

class TopRatedMoviesAdapter(private var mOnUpComingMoviesListener: OnUpComingMoviesListener) :
    RecyclerView.Adapter<TopRatedMoviesAdapter.TopRatedMoviesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ResultXX>() {
        override fun areItemsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class TopRatedMoviesViewHolder(private val itemBinding: MoviePosterItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(resultXX: ResultXX, clickListener: OnUpComingMoviesListener) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${resultXX.poster_path}")
                .placeholder(R.drawable.loding_animation)
                .apply(RequestOptions().override(300, 200))
                .error(R.drawable.poster_placeholder)
                .into(itemBinding.posterImage)

            itemBinding.posterImage.setOnClickListener {
                clickListener.selectedTopRatedMovie(resultXX.id)
            }
        }
    }

    interface OnUpComingMoviesListener {
        fun selectedTopRatedMovie(TopRatedMovieId: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopRatedMoviesAdapter.TopRatedMoviesViewHolder {
        val binding: MoviePosterItemBinding = MoviePosterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopRatedMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TopRatedMoviesAdapter.TopRatedMoviesViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position], mOnUpComingMoviesListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}