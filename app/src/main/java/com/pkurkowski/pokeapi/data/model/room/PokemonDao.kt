package com.pkurkowski.pokeapi.data.model.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDatabaseState(state: DatabaseStateEntity)

    @Transaction
    @Query("SELECT * FROM databaseState WHERE id = 0")
    fun getState(): DatabaseStateEntity?


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemons(pokemons: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonData(pokemonData: PokemonDataEntity)

    @Transaction
    @Query("SELECT * FROM pokemon ORDER BY pokemonId LIMIT :limit OFFSET :offset ")
    fun getPokemon(limit: Int, offset: Int): List<PokemonWithData>

//    @Query("SELECT * FROM pokemon ORDER BY id")
//    fun loadPokemons(): Flow<PokemonEntity>

}


data class PokemonWithData(
    @Embedded val pokemon: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val data: PokemonDataEntity?
)