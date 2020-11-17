package com.pkurkowski.pokeapi.presentation.detail

import com.pkurkowski.pokeapi.domain.Pokemon
import io.uniflow.core.flow.data.UIState

sealed class DetailViewState: UIState() {
    data class LoadingNoData(val pokemonId:Int): DetailViewState()
    data class ShowData(val pokemon: Pokemon): DetailViewState()
    data class Exception(val id: Int, val exception: Throwable): DetailViewState()
}