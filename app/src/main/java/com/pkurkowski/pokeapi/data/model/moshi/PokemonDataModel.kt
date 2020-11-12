package com.pkurkowski.pokeapi.data.model.moshi

data class PokemonDataModel(
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val isDefault: Boolean,
    val order: Int,
    val weight: Int,
    val sprites: PokemonSpritesModel
)

data class PokemonSpritesModel(
    val backDefault: String?,
    val backShiny: String?,
    val frontDefault: String?,
    val frontShiny: String?,
    val backFemale: String?,
    val backShinyFemale: String?,
    val frontFemale: String?,
    val frontShinyFemale: String?
)
