package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies_app.R
import com.example.movies_app.databinding.ActorDetailsItemBinding
import com.example.movies_app.models.CastX

class ActorDetailsAdapter(private var mOnKnownForMovieListener: OnKnownForMovieListener) :
    RecyclerView.Adapter<ActorDetailsAdapter.ActorDetailsViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<CastX>() {
        override fun areItemsTheSame(oldItem: CastX, newItem: CastX): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CastX, newItem: CastX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ActorDetailsViewHolder(private val itemBinding: ActorDetailsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(castX: CastX, clickListener: OnKnownForMovieListener) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${castX.poster_path}")
                .apply(RequestOptions().override(400, 600))
                .placeholder(R.drawable.loding_animation)
                .error(R.drawable.poster_placeholder)
                .into(itemBinding.moviePosterActorDetailsImageView)
            itemBinding.movieNameTV.text = castX.title

            itemBinding.actorConstraintLayout.setOnClickListener {
                clickListener.selectedKnownForMovie(castX.id)
            }
        }
    }

    interface OnKnownForMovieListener {
        fun selectedKnownForMovie(MovieId: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorDetailsAdapter.ActorDetailsViewHolder {
        val binding: ActorDetailsItemBinding = ActorDetailsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActorDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActorDetailsAdapter.ActorDetailsViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position], mOnKnownForMovieListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}