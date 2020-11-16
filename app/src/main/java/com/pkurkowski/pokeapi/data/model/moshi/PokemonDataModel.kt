package com.pkurkowski.pokeapi.data.model.moshi

import com.pkurkowski.pokeapi.data.model.room.PokemonDataEntity

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

fun PokemonDataModel.toEntity(id: Int) = PokemonDataEntity(
    pokemonId = id,
    baseExperience = this.baseExperience,
    height = this.height,
    weight = this.weight,
    isDefault = this.isDefault,

    backDefault = this.sprites.backDefault,
    backShiny = this.sprites.backShiny,
    frontDefault = this.sprites.frontDefault,
    frontShiny = this.sprites.frontShiny,
    backFemale = this.sprites.backFemale,
    backShinyFemale = this.sprites.backShinyFemale,
    frontFemale = this.sprites.frontFemale,
    frontShinyFemale = this.sprites.frontShinyFemale,
)
