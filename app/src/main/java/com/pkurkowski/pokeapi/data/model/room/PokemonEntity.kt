package com.pkurkowski.pokeapi.data.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)], tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "name") val name: String,
)

