package com.pkurkowski.pokeapi.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pkurkowski.pokeapi.application.PokemonPagingSource
import com.pkurkowski.pokeapi.domain.PokemonRepository
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.delay

class PokemonListViewModel(
    val handle: SavedStateHandle,
    private val repository: PokemonRepository
) : AndroidDataFlow() {

    private val pagingData = Pager(
        PagingConfig(
            pageSize = PokemonPagingSource.pokemonsPerPage,
            prefetchDistance = PokemonPagingSource.pokemonsPerPage * 2
        )
    ) {
        PokemonPagingSource(repository)
    }.flow.cachedIn(viewModelScope)


    fun listStateChanged(state: AdapterLoadStateEnum) = action { currentState ->
        //delay results to signal user progress
        if (currentState is PokemonListState.LoadingInitialData) delay(300L)

        setState {
            when (state) {
                AdapterLoadStateEnum.EMPTY_DOING_NOTHING -> PokemonListState.InitialFlowAssign(
                    pagingData
                )
                AdapterLoadStateEnum.EMPTY_LOADING -> PokemonListState.LoadingInitialData
                AdapterLoadStateEnum.EMPTY_ERROR -> PokemonListState.ErrorAndEmptyData
                AdapterLoadStateEnum.FILLED_ADAPTER_WORKING -> PokemonListState.ListWorking
            }
        }
    }
}

enum class AdapterLoadStateEnum {
    EMPTY_DOING_NOTHING,
    EMPTY_LOADING,
    EMPTY_ERROR,
    FILLED_ADAPTER_WORKING
}