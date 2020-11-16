package com.pkurkowski.pokeapi.data.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData

@Entity(indices = [Index(value = ["name", "pokemonId"], unique = true)], tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo(name = "pokemonIndex")  val pokemonIndex:Int,
    @ColumnInfo(name = "pokemonId") val pokemonId: Int?,
    @ColumnInfo(name = "name") val name: String,
)

