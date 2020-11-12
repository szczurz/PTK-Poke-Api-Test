package com.pkurkowski.pokeapi.data.model.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonData")
data class PokemonDataEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val isDefault: Boolean,
    @Embedded val sprites: PokemonSprites
)


data class PokemonSprites(
    val backDefault: String?,
    val backShiny: String?,
    val frontDefault: String?,
    val frontShiny: String?,
    val backFemale: String?,
    val backShinyFemale: String?,
    val frontFemale: String?,
    val frontShinyFemale: String?
)