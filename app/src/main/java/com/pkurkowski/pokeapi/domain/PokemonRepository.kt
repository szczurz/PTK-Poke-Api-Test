package com.pkurkowski.pokeapi.domain

import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): PokemonResponse
    //fun requestPokemonUpdate(target: Pokemon)
}

sealed class PokemonResponse {
    data class Fail(val reason: Throwable): PokemonResponse()
    data class Success(val count: Int, val data: List<Pokemon>): PokemonResponse()
}
