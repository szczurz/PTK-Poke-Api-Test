package com.pkurkowski.pokeapi.data.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PokemonEntity::class, PokemonDataEntity::class, DatabaseStateEntity::class],
    version = 1
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}