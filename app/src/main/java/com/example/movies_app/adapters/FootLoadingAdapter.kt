package com.example.movies_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R

class FootLoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<FootLoadingAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(itemView: View, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val tvErrorMessage: TextView = itemView.findViewById(R.id.tvErrorMessage)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val btnRetry: Button = itemView.findViewById(R.id.btnRetry)

        init {
            btnRetry.setOnClickListener {
                retry.invoke()
            }
        }


        fun bindState(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                tvErrorMessage.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            tvErrorMessage.isVisible = loadState !is LoadState.Loading
            btnRetry.isVisible = loadState !is LoadState.Loading

        }

    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading_state, parent, false)
        return LoadingStateViewHolder(view, retry)
    }
}