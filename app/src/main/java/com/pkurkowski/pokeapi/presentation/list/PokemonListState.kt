package com.pkurkowski.pokeapi.presentation.list

import androidx.paging.PagingData
import com.pkurkowski.pokeapi.presentation.list.adapter.PokemonWithUpdate
import io.uniflow.core.flow.data.UIState

sealed class PokemonListState: UIState() {
    data class InitialFlowAssign(val data: PagingData<PokemonWithUpdate>): PokemonListState()
    object LoadingInitialData: PokemonListState()
    object ErrorAndEmptyData: PokemonListState()
    object ListWorking: PokemonListState()
}