package com.pkurkowski.pokeapi.data.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PokemonDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonData(users: PokemonDataEntity)

}