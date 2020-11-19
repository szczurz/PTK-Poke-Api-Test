package com.pkurkowski.pokeapi.presentation.list

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pkurkowski.pokeapi.application.PokemonPagingSource
import com.pkurkowski.pokeapi.domain.*
import com.pkurkowski.pokeapi.presentation.list.adapter.PokemonWithUpdate
import com.pkurkowski.pokeapi.presentation.list.adapter.UpdateRequestData
import com.pkurkowski.pokeapi.presentation.list.adapter.UpdateStatus
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonListViewModel(
    private val repository: PokemonRepository
) : AndroidDataFlow() {

    private val updatesFlow = MutableStateFlow(mapOf<Int, UpdateStatus>())
    private val updatesMap = mutableMapOf<Int, UpdateStatus>()

    val updatePokemonChannel = Channel<UpdateRequestData>(
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
        .map {
            it.map { pokemon ->
                val update = updatesFlow.value[pokemon.index] ?: UpdateStatus.Empty
                PokemonWithUpdate(pokemon, update)
            }
        }

    init {
        viewModelScope.launch {
            updatePokemonChannel.consumeAsFlow()
                .map { requestData ->
                    sendPokemonUpdateEvent(requestData.pokemonIndex, UpdateStatus.InProgress)
                    Pair(repository.requestPokemon(requestData.pokemonId), requestData.pokemonIndex)
                }
                .collect { (response, pokemonIndex) ->

                    val basicData =
                        (response as? PokemonResponse.Success)?.pokemon?.getSBasicDataOrNull()

                    if (basicData != null) {
                        sendPokemonUpdateEvent(pokemonIndex, UpdateStatus.Updated(basicData))
                    } else {
                        sendPokemonUpdateEvent(pokemonIndex, UpdateStatus.Empty)
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

    fun sendPokemonUpdateEvent(pokemonIndex: Int, status: UpdateStatus) = action {
        updatesMap[pokemonIndex] = status
        updatesFlow.emit(updatesMap.toMap())
        sendEvent(PokemonListEvent.PokemonUpdatedEvent(pokemonIndex, status))
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