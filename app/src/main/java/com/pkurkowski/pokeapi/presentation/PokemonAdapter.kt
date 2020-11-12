package com.pkurkowski.pokeapi.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.Pokemon

class PokemonAdapter :
    PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonComparator) {

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        private val idTextView: TextView = view.findViewById(R.id.idTextView)
        val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
        fun bind(pokemon: Pokemon?) {
            nameTextView.text = pokemon?.name ?: ""
            idTextView.text = pokemon?.id?.toString() ?: ""
        }

    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon_list, parent, false)
        return PokemonViewHolder(view)
    }
}

object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }
}