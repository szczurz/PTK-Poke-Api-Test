package com.pkurkowski.pokeapi.domain

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): PokemonsResponse
    suspend fun requestPokemonUpdate(id: Int): PokemonResponse
}

sealed class PokemonsResponse {
    data class Fail(val reason: Throwable): PokemonsResponse()
    data class Success(val data: List<Pokemon>, val hasNext: Boolean): PokemonsResponse()
}

sealed class PokemonResponse {
    data class Fail(val reason: Throwable): PokemonResponse()
    data class Success(val data: Pokemon): PokemonResponse()
}
