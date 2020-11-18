package com.pkurkowski.pokeapi.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.presentation.list.AdapterLoadStateEnum
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map

class PokemonAdapter(private val clickListener: (Pokemon) -> Unit, private val updateChannel: Channel<Int>) :
    PagingDataAdapter<PokemonWithUpdate, PokemonAdapter.PokemonViewHolder>(PokemonComparator) {

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

    fun updatePokemon(index: Int, data: PokemonData.PokemonBasicData) {
        getItem(index)?.let {
            it.update = data
            notifyItemChanged(index)
        }
    }

    inner class PokemonViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon_list, parent, false)
    ) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val indexTextView: TextView = itemView.findViewById(R.id.indexTextView)
        private val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        private var job: Job? = null

        fun bind(pokemon: PokemonWithUpdate?) {
            job?.cancel()

            nameTextView.text = pokemon?.original?.name ?: ""
            indexTextView.text = pokemon?.original?.index.toString() ?: "--"
            idTextView.text = pokemon?.original?.pokemonId?.toString() ?: "--"
            progressBar.isVisible = false

            if (pokemon == null) {
                itemView.setOnClickListener(null)
            } else {
                itemView.setOnClickListener { clickListener.invoke(pokemon.original) }
            }

            val basicData: PokemonData.PokemonBasicData? = when {
                pokemon?.update != null -> pokemon.update
                else -> pokemon?.original?.data as? PokemonData.PokemonBasicData
            }

            if (basicData == null) {
                sendUpdateRequestIfNeeded(pokemon?.original?.pokemonId)
                iconImageView.setImageDrawable(null)
            } else {
                iconImageView.setImageResource(R.drawable.ic_baseline_sync_24)
            }
        }

        fun unbind() {
            job?.cancel()
        }

        private fun sendUpdateRequestIfNeeded(pokemonId: Int?) {
            pokemonId?.let {
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(DELAY_TO_PROCESS_UPDATE_MILLISECONDS)
                    updateChannel.send(it)
                    progressBar.isVisible = true
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
        holder.unbind()
        return super.onFailedToRecycleView(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(parent)
    }

    companion object {
        const val DELAY_TO_PROCESS_UPDATE_MILLISECONDS = 600L
    }
}

object PokemonComparator : DiffUtil.ItemCallback<PokemonWithUpdate>() {
    override fun areItemsTheSame(oldItem: PokemonWithUpdate, newItem: PokemonWithUpdate): Boolean {
        // index unique.
        return oldItem.original.index == newItem.original.index
    }

    override fun areContentsTheSame(
        oldItem: PokemonWithUpdate,
        newItem: PokemonWithUpdate
    ): Boolean {
        return oldItem == newItem
    }


}