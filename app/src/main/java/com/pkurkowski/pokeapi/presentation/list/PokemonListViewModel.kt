package com.pkurkowski.pokeapi.presentation.list

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pkurkowski.pokeapi.application.PokemonPagingSource
import com.pkurkowski.pokeapi.domain.*
import com.pkurkowski.pokeapi.presentation.list.adapter.PokemonWithUpdate
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val repository: PokemonRepository
) : AndroidDataFlow() {

    val updatePokemonChannel = Channel<Int>(
        capacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )


    private val pagingData = Pager(
        PagingConfig(
            pageSize = PokemonPagingSource.pokemonsPerPage,
            prefetchDistance = PokemonPagingSource.pokemonsPerPage,
            enablePlaceholders = false
        )
    ) {
        PokemonPagingSource(repository)
    }.flow
        .cachedIn(viewModelScope)
        .map { it.map { pokemon -> PokemonWithUpdate(pokemon) } }

    init {
        viewModelScope.launch {
            updatePokemonChannel.consumeAsFlow()
                .map {
                    repository.requestPokemon(it)
                }
                .collect {
                    if (it is PokemonResponse.Success) {
                        it.pokemon.getSBasicDataOrNull()?.let { basicData ->
                            sendPokemonUpdateEvent(it.pokemon.index, basicData)
                        }
                    }
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


    fun sendPokemonClickedEvent(pokemonId: Int) = action {
        sendEvent(PokemonListEvent.PokemonSelectedEvent(pokemonId))
    }

    fun sendPokemonUpdateEvent(pokemonIndex: Int, data: PokemonData.PokemonBasicData) = action {
        sendEvent(PokemonListEvent.PokemonUpdatedEvent(pokemonIndex, data))
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