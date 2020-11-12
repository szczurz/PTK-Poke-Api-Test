package com.pkurkowski.pokeapi.presentation

sealed class PokemonListState {
    object LoadingInitialData: PokemonListState()
    data class ErrorAndEmptyData(val reason: Throwable): PokemonListState()
}