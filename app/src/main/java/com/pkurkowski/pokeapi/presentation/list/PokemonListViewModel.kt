package com.pkurkowski.pokeapi.presentation.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pkurkowski.pokeapi.application.PokemonPagingSource
import com.pkurkowski.pokeapi.domain.PokemonRepository
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PokemonListViewModel(
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

    fun startInitialState() = action {
        viewModelScope.launch {
            pagingData.collect { data ->
                action {
                    setState { PokemonListState.InitialFlowAssign(data) }
                }
            }
        }
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

    fun requestPokemonUpdate(pokemonId: Int) = action {
        repository.requestPokemonUpdate(pokemonId)
    }

}

enum class AdapterLoadStateEnum {
    EMPTY_DOING_NOTHING,
    EMPTY_LOADING,
    EMPTY_ERROR,
    FILLED_ADAPTER_WORKING
}