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
import com.example.movies_app.databinding.CasteItemBinding
import com.example.movies_app.models.Cast

class CastAdapter(private var mOnCastListener: OnCastListener) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class CastViewHolder (private val itemBinding: CasteItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(cast: Cast, clickListener: OnCastListener) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${cast.profile_path}")
                .placeholder(R.drawable.loding_animation)
                .apply(RequestOptions().override(400,600))
                .error(R.drawable.person_placeholder)
                .into(itemBinding.actorImage)

            val width = itemBinding.actorImage.measuredWidth
            val height = itemBinding.actorImage.measuredHeight
            Log.e("test " ,"the width is $width , the height is $height")
            itemBinding.actorName.text = cast.name
            itemBinding.characterName.text = cast.character
            itemBinding.actorImage.setOnClickListener {
                clickListener.selectedActor(cast.id)
            }
        }
    }
    interface OnCastListener {
        fun selectedActor(ActorId: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CastAdapter.CastViewHolder {
        val binding: CasteItemBinding = CasteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastAdapter.CastViewHolder, position: Int) {
        holder.bind(differ.currentList[position] , mOnCastListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}