package com.pkurkowski.pokeapi.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pkurkowski.pokeapi.BuildConfig
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.presentation.list.AdapterLoadStateEnum
import kotlinx.coroutines.flow.map

class PokemonAdapter(val clickListener: (Pokemon) -> Unit) :
    PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonComparator) {

    val loadStateEnum = loadStateFlow
        .map { it.refresh }
        .map {state ->
        if (itemCount == 0) {
            when (state) {
                is LoadState.NotLoading -> AdapterLoadStateEnum.EMPTY_DOING_NOTHING
                is LoadState.Loading -> AdapterLoadStateEnum.EMPTY_LOADING
                is LoadState.Error -> AdapterLoadStateEnum.EMPTY_ERROR
            }
        } else {
            AdapterLoadStateEnum.FILLED_ADAPTER_WORKING
        }
    }


    inner class PokemonViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon_list, parent, false)
    ) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val indexTextView: TextView = itemView.findViewById(R.id.indexTextView)
        private val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)

        fun bind(pokemon: Pokemon?) {
            pokemon?.let { pokemon ->
                itemView.setOnClickListener { clickListener.invoke(pokemon) }
            }

            nameTextView.text = pokemon?.name ?: ""
            indexTextView.text = pokemon?.index?.toString() ?: "--"
            idTextView.text = pokemon?.pokemonId?.toString() ?: "--"

            pokemon?.pokemonId?.let {
                Glide.with(iconImageView)
                    .load(
                        BuildConfig.POKE_IMAGE_PATTERN_URL.replace(
                            "ID_TO_REPLACE",
                            it.toString(),
                            false
                        )
                    )
                    .placeholder(R.drawable.ic_baseline_sync_24)
                    .error(R.drawable.ic_baseline_sync_problem_24)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(iconImageView)
            }
        }
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(parent)
    }

}

object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        // Id is unique.
        return oldItem.index == newItem.index && oldItem.pokemonId == newItem.pokemonId
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }
}