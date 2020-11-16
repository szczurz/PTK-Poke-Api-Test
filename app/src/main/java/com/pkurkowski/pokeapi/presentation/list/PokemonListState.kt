package com.pkurkowski.pokeapi.presentation.list

import androidx.paging.PagingData
import com.pkurkowski.pokeapi.domain.Pokemon
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.flow.Flow

sealed class PokemonListState: UIState() {
    data class InitialFlowAssign(val data: PagingData<Pokemon>): PokemonListState()
    object LoadingInitialData: PokemonListState()
    object ErrorAndEmptyData: PokemonListState()
    object ListWorking: PokemonListState()
}