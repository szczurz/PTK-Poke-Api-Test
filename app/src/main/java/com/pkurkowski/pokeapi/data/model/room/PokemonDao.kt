package com.pkurkowski.pokeapi.data.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(vararg pokemons: PokemonEntity)

    @Query("SELECT * FROM pokemon ORDER BY id")
    fun loadPokemons(): Flow<PokemonEntity>

}