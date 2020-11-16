package com.pkurkowski.pokeapi.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isInvisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pkurkowski.pokeapi.R

class PokemonLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PokemonLoadStateAdapter.PokemonLoadStateViewHolder>() {

    class PokemonLoadStateViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon_list_load_state, parent, false)
    ) {
        private val errorTextView: TextView = itemView.findViewById(R.id.errorTextView)
        private val errorImageView: ImageView = itemView.findViewById(R.id.errorImageView)
        private val progress: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(loadState: LoadState, retry: () -> Unit) {
            errorTextView.isInvisible = loadState !is LoadState.Error
            errorImageView.isInvisible = loadState !is LoadState.Error
            progress.isInvisible = loadState !is LoadState.Loading

            if (loadState is LoadState.Error) {
                itemView.setOnClickListener { retry() }
            } else {
                itemView.setOnClickListener(null)
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = PokemonLoadStateViewHolder(parent)

    override fun onBindViewHolder(
        holder: PokemonLoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState, retry)

}