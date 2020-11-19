package com.pkurkowski.pokeapi.domain

data class Pokemon(
    val index: Int,
    val pokemonId: Int?,
    val name: String,
    val data: PokemonData,
    )


sealed class PokemonData {
    object Empty: PokemonData()
    data class PokemonBasicData(
        val baseExperience: Int,
        val height: Int,
        val weight: Int,
        val isDefault: Boolean,
        val sprites: PokemonSprites,
        val types: String
    ): PokemonData()
}


fun Pokemon.getSBasicDataOrNull(): PokemonData.PokemonBasicData? =
    when(this.data) {
        PokemonData.Empty -> null
        is PokemonData.PokemonBasicData -> this.data
    }