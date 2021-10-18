package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies_app.R
import com.example.movies_app.databinding.SearchMoviesItemBinding
import com.example.movies_app.models.ResultXXXX

class SearchMoviesAdapter(private var mOnSearchMoviesListener: OnSearchMoviesListener) :
    RecyclerView.Adapter<SearchMoviesAdapter.SearchMoviesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ResultXXXX>() {
        override fun areItemsTheSame(oldItem: ResultXXXX, newItem: ResultXXXX): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ResultXXXX, newItem: ResultXXXX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class SearchMoviesViewHolder(private val itemBinding: SearchMoviesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(resultXXXX: ResultXXXX, clickListener: OnSearchMoviesListener) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${resultXXXX.poster_path}")
                .error(R.drawable.poster_placeholder)
                .placeholder(R.drawable.loding_animation)
                .apply(RequestOptions().override(150, 250))
                .into(itemBinding.searchMoviePoster)
            itemBinding.searchMovieName.text = resultXXXX.title
            itemBinding.searchMovieDate.text = resultXXXX.release_date
            itemBinding.searchMoviesConstraintLayout.setOnClickListener {
                clickListener.selectedSearchMovie(resultXXXX.id)
            }
        }
    }

    interface OnSearchMoviesListener {
        fun selectedSearchMovie(MovieId: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchMoviesAdapter.SearchMoviesViewHolder {
        val binding: SearchMoviesItemBinding = SearchMoviesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchMoviesAdapter.SearchMoviesViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position], mOnSearchMoviesListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}