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
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.presentation.list.AdapterLoadStateEnum
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map

class PokemonAdapter(val clickListener: (Pokemon) -> Unit, val updateChannel: Channel<Pokemon>) :
    PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonComparator) {

    val loadStateEnum = loadStateFlow
        .map { it.refresh }
        .map { state ->
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

        private var job: Job? = null


        fun bind(pokemon: Pokemon?) {
            pokemon?.let { pokemon ->
                itemView.setOnClickListener { clickListener.invoke(pokemon) }
            }

            nameTextView.text = pokemon?.name ?: ""
            indexTextView.text = pokemon?.index?.toString() ?: "--"
            idTextView.text = pokemon?.pokemonId?.toString() ?: "--"

            if(pokemon?.data is PokemonData.Empty) iconImageView.setImageDrawable(null)
            else iconImageView.setImageResource(R.drawable.ic_baseline_sync_24)


            job?.cancel()
            pokemon?.also {sendUpdateRequestIfNeeded(it)}
        }

        fun unbind() {
            job?.cancel()
        }

        private fun sendUpdateRequestIfNeeded(pokemon: Pokemon) {
            if (pokemon.data == PokemonData.Empty) {
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(DELAY_TO_PROCESS_UPDATE_MILLISECONDS)
                    updateChannel.send(pokemon)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: PokemonViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun onViewDetachedFromWindow(holder: PokemonViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unbind()
    }

    override fun onFailedToRecycleView(holder: PokemonViewHolder): Boolean {
        return super.onFailedToRecycleView(holder)
        holder.unbind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(parent)
    }

    companion object {
        const val DELAY_TO_PROCESS_UPDATE_MILLISECONDS = 600L
    }
}

object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        // Id is unique.
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        //todo
        return false
    }


}