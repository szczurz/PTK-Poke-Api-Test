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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonListViewModel(
    private val repository: PokemonRepository
) : AndroidDataFlow() {

    val updatePokemonChannel = Channel<Int>(
        capacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
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
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            updatePokemonChannel.consumeAsFlow()
                .map {
                    repository.requestPokemon(it)
                }.debounce(1000L)
                .collect {
                pokemonPagingSource?.invalidate()
            }
        }

//        viewModelScope.launch {
//
//            while (isActive) {
//                delay(3000L)
//                Timber.tag("ZZZZZ").d("INVALIDATE !!!")
//                pokemonPagingSource?.invalidate()
//            }
//
//        }
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
        //pokemonPagingSource?.invalidate()
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