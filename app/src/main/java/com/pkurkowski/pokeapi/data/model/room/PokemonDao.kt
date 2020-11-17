package com.pkurkowski.pokeapi.data.model.room

import androidx.room.*
import com.pkurkowski.pokeapi.data.model.moshi.toEntity
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.domain.PokemonSprites
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
    @Query("SELECT * FROM pokemon ORDER BY pokemonIndex LIMIT :limit OFFSET :offset ")
    fun getPokemons(limit: Int, offset: Int): List<PokemonWithData>

    @Query("SELECT * FROM pokemon WHERE pokemonId = :pokemonId")
    fun getPokemon(pokemonId: Int): PokemonWithData?

}


data class PokemonWithData(
    @Embedded val pokemon: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId"
    )
    val data: PokemonDataEntity?
)

fun PokemonWithData.toPokemon() = Pokemon(
    index = this.pokemon.pokemonIndex,
    pokemonId = this.pokemon.pokemonId,
    name = this.pokemon.name,
    data = when(data) {
        null -> PokemonData.Empty
        else -> PokemonData.PokemonBasicData(
            baseExperience = data.baseExperience,
            height = data.height,
            weight = data.weight,
            isDefault = data.isDefault,
            sprites = PokemonSprites(
                backDefault = this.data.backDefault,
                backShiny = this.data.backShiny,
                frontDefault = this.data.frontDefault,
                frontShiny = this.data.frontShiny,
                backFemale = this.data.backFemale,
                backShinyFemale = this.data.backShinyFemale,
                frontFemale = this.data.frontFemale,
                frontShinyFemale = this.data.frontShinyFemale,
            )
        )
    }
)