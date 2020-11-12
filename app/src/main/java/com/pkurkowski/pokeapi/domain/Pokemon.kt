package com.pkurkowski.pokeapi.domain

data class Pokemon(
    val id: Int,
    val name: String,
    val data: PokemonData,
)


sealed class PokemonData {
    object Empty: PokemonData()
    data class PokemonBasicData(
        val baseExperience: Int,
        val height: Int,
        val isDefault: Boolean,
        val order: Int,
        val weight: Int,
        val sprites: PokemonSprites
    )
}