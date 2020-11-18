package com.pkurkowski.pokeapi.presentation.list

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pkurkowski.pokeapi.application.PokemonPagingSource
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonRepository
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonListViewModel(
    private val repository: PokemonRepository
) : AndroidDataFlow() {

    val updatePokemonChannel = Channel<Pokemon>(
        capacity = 12,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var pokemonPagingSource: PokemonPagingSource? = null

    private val pagingData = Pager(
        PagingConfig(
            pageSize = PokemonPagingSource.pokemonsPerPage,
            prefetchDistance = PokemonPagingSource.pokemonsPerPage,
            enablePlaceholders = true
        )
    ) {
        pokemonPagingSource = PokemonPagingSource(repository)
        pokemonPagingSource!!
    }.flow.cachedIn(viewModelScope).map {

        Timber.tag("ZZZZZ").d("------------------- BOOM pagindData Emission")

        it
    }

    init {
        viewModelScope.launch {
            updatePokemonChannel.consumeAsFlow().collect { pokemon ->
                Timber.d("POKEMON to update: ${pokemon.name}")
                var result = pokemon.pokemonId?.let { repository.requestPokemon(it) }
                Timber.d("POKEMON updated result: $result")
            }
        }
    }

    fun startInitialState() = action {
        viewModelScope.launch {
            pagingData.collectLatest { data ->
                action {
                    setState { PokemonListState.InitialFlowAssign(data) }
                }
            }
        }
    }

    fun pokemonClicked(id: Int) {
        pokemonPagingSource?.invalidate()
    }

    fun listStateChanged(state: AdapterLoadStateEnum) = action { currentState ->
        //delay results to signal user progress
        if (currentState is PokemonListState.LoadingInitialData) delay(300L)

        when (state) {
            AdapterLoadStateEnum.EMPTY_LOADING -> setState { PokemonListState.LoadingInitialData }
            AdapterLoadStateEnum.EMPTY_ERROR -> setState { PokemonListState.ErrorAndEmptyData }
            AdapterLoadStateEnum.FILLED_ADAPTER_WORKING -> setState { PokemonListState.ListWorking }
        }
    }


}

enum class AdapterLoadStateEnum {
    EMPTY_DOING_NOTHING,
    EMPTY_LOADING,
    EMPTY_ERROR,
    FILLED_ADAPTER_WORKING
}