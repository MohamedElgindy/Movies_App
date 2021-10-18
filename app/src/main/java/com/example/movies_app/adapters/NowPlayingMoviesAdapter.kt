package com.example.movies_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies_app.R
import com.example.movies_app.databinding.MoviePosterItemBinding
import com.example.movies_app.models.ResultX

class NowPlayingMoviesAdapter(private var mOnNowPlayingMovieListener: OnNowPlayingMovieListener) :
    RecyclerView.Adapter<NowPlayingMoviesAdapter.NowPlayingMoviesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ResultX>() {
        override fun areItemsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class NowPlayingMoviesViewHolder(private val itemBinding: MoviePosterItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(resultx: ResultX, clickListener: OnNowPlayingMovieListener) {
            //itemBinding.subjectName.text = subjectName
            Log.e("frr", resultx.id.toString())
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${resultx.poster_path}")
                .placeholder(R.drawable.loding_animation)
                .apply(RequestOptions().override(300, 200))
                .error(R.drawable.poster_placeholder)
                .into(itemBinding.posterImage)
            itemBinding.posterImage.setOnClickListener {
                clickListener.selectedNowPlayingMovie(resultx.id)
            }
        }
    }

    interface OnNowPlayingMovieListener {
        fun selectedNowPlayingMovie(MovieId: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NowPlayingMoviesAdapter.NowPlayingMoviesViewHolder {
        val binding: MoviePosterItemBinding = MoviePosterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NowPlayingMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NowPlayingMoviesAdapter.NowPlayingMoviesViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position], mOnNowPlayingMovieListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}