package com.pkurkowski.pokeapi.data.model.moshi

import com.pkurkowski.pokeapi.data.model.room.PokemonDataEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDataModel(
    val name: String,
    @Json(name = "base_experience") val baseExperience: Int,
    val height: Int,
    @Json(name = "is_default") val isDefault: Boolean,
    val order: Int,
    val weight: Int,
    val sprites: PokemonSpritesModel
)

@JsonClass(generateAdapter = true)
data class PokemonSpritesModel(
    @Json(name = "back_default") val backDefault: String?,
    @Json(name = "back_shiny") val backShiny: String?,
    @Json(name = "front_default") val frontDefault: String?,
    @Json(name = "front_shiny") val frontShiny: String?,
    @Json(name = "back_female") val backFemale: String?,
    @Json(name = "back_shiny_female") val backShinyFemale: String?,
    @Json(name = "front_female") val frontFemale: String?,
    @Json(name = "front_shiny_female") val frontShinyFemale: String?,
    @Json(name = "other") val other: SpritesOtherModel
)

@JsonClass(generateAdapter = true)
data class SpritesOtherModel(
    @Json(name = "dream_world") val dreamWord: DreamWorkSpritesModel,
    @Json(name = "official-artwork") val officialArtwork: OfficialArtworkSpritesModel
)

@JsonClass(generateAdapter = true)
data class DreamWorkSpritesModel(
    @Json(name = "front_default") val frontDefault: String?,
    @Json(name = "front_female") val frontFemale: String?,
)

@JsonClass(generateAdapter = true)
data class OfficialArtworkSpritesModel(
    @Json(name = "front_default") val frontDefault: String?,
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

    dreamWorkFrontMale = this.sprites.other.dreamWord.frontDefault,
    dreamWorkFrontFemale = this.sprites.other.dreamWord.frontFemale,

    officialFront = this.sprites.other.officialArtwork.frontDefault

)
