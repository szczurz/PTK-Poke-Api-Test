package com.pkurkowski.pokeapi.data.repository

import com.pkurkowski.pokeapi.data.model.retrofit.PokeApiInterface
import com.pkurkowski.pokeapi.data.model.room.DatabaseStateEntity
import com.pkurkowski.pokeapi.data.model.room.PokemonDao
import com.pkurkowski.pokeapi.data.model.room.PokemonEntity
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

class PokemonRepositoryImp(
    val api: PokeApiInterface,
    val pokemonDao: PokemonDao,
) : PokemonRepository {


    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonResponse {
        return withContext(Dispatchers.IO) {

            val state = pokemonDao.getState() ?: DatabaseStateEntity()

            if (state.pokemonLoadedCount >= offset + limit) {

                Timber.d("database read offset:  $offset")

                PokemonResponse.Success(
                    hasNext = state.pokemonCount ?: Int.MAX_VALUE > offset + limit,
                    data = pokemonDao.getPokemon(limit, offset)
                        .mapIndexed { index, pokemonWithData ->
                            Pokemon(index + offset, pokemonWithData.pokemon.name, PokemonData.Empty)
                        }

                )
            } else {

                try {

                    Timber.d("retrofit read offset:  $offset")

                    val response = api.getPokemons(limit, offset).execute()

                    when (response.isSuccessful) {

                        false -> {
                            PokemonResponse.Fail(Exception("Call unsuccessful body: ${response.errorBody()}"))
                        }

                        true -> {

                            val pokemonData = response.body()!!

                            pokemonDao.insertPokemons(
                                pokemonData.results.mapIndexed { index, pokemonModel ->
                                    PokemonEntity(index + offset, pokemonModel.name)
                                }
                            )

                            pokemonDao.insertDatabaseState(
                                DatabaseStateEntity(
                                    pokemonCount = pokemonData.count,
                                    pokemonLoadedCount = offset + pokemonData.results.size
                                )
                            )

                            PokemonResponse.Success(
                                hasNext = pokemonData.results.size + offset < pokemonData.count,
                                data = pokemonData.results.mapIndexed { index, pokemonModel ->
                                    Pokemon(index + offset, pokemonModel.name, PokemonData.Empty)
                                }
                            )
                        }
                    }

                } catch (e: Exception) {
                    PokemonResponse.Fail(e)
                }

            }
        }
    }

}


