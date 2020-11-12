package com.pkurkowski.pokeapi.data.model.moshi

data class PokemonsResponseModel(
    val count: Int,
    val results: List<PokemonModel>,
)