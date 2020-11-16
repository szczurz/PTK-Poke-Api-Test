package com.pkurkowski.pokeapi.presentation

import androidx.paging.PagingData
import com.pkurkowski.pokeapi.domain.Pokemon
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.flow.Flow

sealed class PokemonListState: UIState() {
    data class InitialFlowAssign(val data: Flow<PagingData<Pokemon>>): PokemonListState()
    object LoadingInitialData: PokemonListState()
    object ErrorAndEmptyData: PokemonListState()
    object ListWorking: PokemonListState()
}