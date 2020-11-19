package com.pkurkowski.pokeapi.data.model.room

import androidx.room.*
import com.pkurkowski.pokeapi.domain.*

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
            sprites = this.toPokemonSprites()
        )
    }
)

fun PokemonWithData.toPokemonSprites(): PokemonSprites {
    return when (this.data) {
        null -> PokemonSprites(mapOf())
        else -> {

            //todo implement some kind of builder here
            val map = mutableMapOf<SpriteDescription, String>()

            this.data.officialFront?.let { map.put(
                SpriteDescription(Side.Front, Gender.Male, Style.Regular, Source.Official), it) }

            with(this.data) {
                dreamWorkFrontMale?.let { map.put(
                    SpriteDescription(Side.Front, Gender.Male, Style.Regular, Source.DreamWorks), it) }
                dreamWorkFrontFemale?.let { map.put(
                    SpriteDescription(Side.Front, Gender.Female, Style.Regular, Source.DreamWorks), it) }
            }

            with(this.data) {
                backDefault?.let { map.put(
                    SpriteDescription(Side.Back, Gender.Male, Style.Regular, Source.Regular), it) }
                backShiny?.let { map.put(
                    SpriteDescription(Side.Back, Gender.Male, Style.Shiny, Source.Regular), it) }
                frontDefault?.let { map.put(
                    SpriteDescription(Side.Front, Gender.Male, Style.Regular, Source.Regular), it) }
                frontShiny?.let { map.put(
                    SpriteDescription(Side.Front, Gender.Male, Style.Shiny, Source.Regular), it) }
                backFemale?.let { map.put(
                    SpriteDescription(Side.Back, Gender.Female, Style.Regular, Source.Regular), it) }
                backShinyFemale?.let { map.put(
                    SpriteDescription(Side.Back, Gender.Female, Style.Shiny, Source.Regular), it) }
                frontFemale?.let { map.put(
                    SpriteDescription(Side.Front, Gender.Female, Style.Regular, Source.Regular), it) }
                frontShinyFemale?.let { map.put(
                    SpriteDescription(Side.Front, Gender.Female, Style.Shiny, Source.Regular), it) }
            }



            PokemonSprites(
                map.toMap()
            )

            /**
             *                 dreamWorks.toMap(),
            this.data.officialFront?.let { Pair(SpriteDescription(Side.Front, Gender.Male, Style.Regular), it) }
             */


        }
    }
}
