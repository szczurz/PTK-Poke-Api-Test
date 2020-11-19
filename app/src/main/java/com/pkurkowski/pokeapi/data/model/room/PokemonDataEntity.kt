package com.pkurkowski.pokeapi.data.model.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonData")
data class PokemonDataEntity(
    @PrimaryKey @ColumnInfo(name = "pokemonId") val pokemonId: Int,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val isDefault: Boolean,

    val backDefault: String?,
    val backShiny: String?,
    val frontDefault: String?,
    val frontShiny: String?,
    val backFemale: String?,
    val backShinyFemale: String?,
    val frontFemale: String?,
    val frontShinyFemale: String?,

    val dreamWorkFrontMale: String?,
    val dreamWorkFrontFemale: String?,

    val officialFront: String?,
)


