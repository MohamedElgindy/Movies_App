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
import com.example.movies_app.models.Result

class UpComingMoviesAdapter(private var mOnUpComingMoviesListener: OnUpComingMoviesListener) :
    RecyclerView.Adapter<UpComingMoviesAdapter.UpComingMoviesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class UpComingMoviesViewHolder(private val itemBinding: MoviePosterItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(result: Result, clickListener: OnUpComingMoviesListener) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${result.poster_path}")
                .apply(RequestOptions().override(300, 200))
                .placeholder(R.drawable.loding_animation)
                .error(R.drawable.poster_placeholder)
                .into(itemBinding.posterImage)
            //itemBinding.subjectName.text = subjectName


            itemBinding.posterImage.setOnClickListener {
                clickListener.selectedUpComingMovieId(result.id)
            }
        }
    }

    interface OnUpComingMoviesListener {
        fun selectedUpComingMovieId(upComingMovieId: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpComingMoviesViewHolder {
        val binding: MoviePosterItemBinding = MoviePosterItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpComingMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpComingMoviesViewHolder, position: Int) {
        holder.bind(differ.currentList[position], mOnUpComingMoviesListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}