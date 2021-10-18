package com.example.movies_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.movies_app.R
import com.example.movies_app.databinding.SlideItemContainerBinding
import com.example.movies_app.models.MoviesModel


class PopularMovieSliderAdapter internal constructor(
    private var mOnPopularMoviesListener: OnPopularMoviesListener,
    private val sliderItems: MutableList<MoviesModel>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<PopularMovieSliderAdapter.PopularMovieSliderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularMovieSliderViewHolder {
        val binding: SlideItemContainerBinding = SlideItemContainerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularMovieSliderViewHolder(binding)

    }

    override fun onBindViewHolder(holderPopularMovie: PopularMovieSliderViewHolder, position: Int) {
        holderPopularMovie.setImage(sliderItems[position], mOnPopularMoviesListener)
        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class PopularMovieSliderViewHolder(private val itemBinding: SlideItemContainerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun setImage(sliderItems: MoviesModel, clickListener: OnPopularMoviesListener) {

            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${sliderItems.poster_path}")
                .placeholder(R.drawable.loding_animation)
                .error(R.drawable.poster_placeholder)
                .into(itemBinding.popularMoviePosterImageView)
            val height = itemView.measuredHeight
            val width = itemView.measuredWidth
            Log.e("test " ," height  is $height width is $width")
            itemBinding.popularMoviePosterImageView.setOnClickListener {
                clickListener.selectedPopularMovieName(sliderItems.id)
            }
        }

    }

    interface OnPopularMoviesListener {
        fun selectedPopularMovieName(popularMovieId: Int)
    }

    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

}
