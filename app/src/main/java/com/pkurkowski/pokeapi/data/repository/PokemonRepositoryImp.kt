package com.pkurkowski.pokeapi.data.repository

import com.pkurkowski.pokeapi.data.model.moshi.toEntity
import com.pkurkowski.pokeapi.data.model.moshi.toPokemon
import com.pkurkowski.pokeapi.data.model.retrofit.PokeApiInterface
import com.pkurkowski.pokeapi.data.model.room.*
import com.pkurkowski.pokeapi.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PokemonRepositoryImp(
    private val api: PokeApiInterface,
    private val pokemonDao: PokemonDao,
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonsResponse {

        Timber.tag("ZZZZZ").d("-- Repo getPokemonList offset: $offset  limit: $limit")

        return withContext(Dispatchers.IO) {
            val state = pokemonDao.getState() ?: DatabaseStateEntity()
            if (state.pokemonLoadedCount >= offset + limit) {
                Timber.d("database pokemon list read offset:  $offset")
                PokemonsResponse.Success(
                    hasNext = state.pokemonCount ?: Int.MAX_VALUE > offset + limit,
                    data = pokemonDao.getPokemons(limit, offset)
                        .map { it.toPokemon() }
                )
            } else {
                try {
                    Timber.d("retrofit pokemon list read offset:  $offset")
                    val response = api.getPokemons(limit, offset).execute()

                    when (response.isSuccessful) {
                        false -> {
                            PokemonsResponse.Fail(Exception("Call unsuccessful response: $response"))
                        }

                        true -> {
                            val pokemonData = response.body()!!
                            pokemonDao.insertPokemons(
                                pokemonData.results.mapIndexed { index, pokemonModel ->
                                    pokemonModel.toEntity(index + offset)
                                }
                            )
                            //todo review pokemonLoadedCount works
                            pokemonDao.insertDatabaseState(
                                DatabaseStateEntity(
                                    pokemonCount = pokemonData.count,
                                    pokemonLoadedCount = offset + pokemonData.results.size
                                )
                            )
                            PokemonsResponse.Success(
                                hasNext = pokemonData.results.size + offset < pokemonData.count,
                                data = pokemonData.results.mapIndexed { index, pokemonModel ->
                                    pokemonModel.toPokemon(index + offset)
                                }
                            )
                        }
                    }

                } catch (e: Exception) {
                    PokemonsResponse.Fail(e)
                }
            }
        }
    }

    override suspend fun requestPokemon(id: Int): PokemonResponse {
        return withContext(Dispatchers.IO) {
            pokemonDao.getPokemon(id)
                ?.let {
                    if(it.data != null) {
                        PokemonResponse.Success(it.toPokemon())
                    } else null
                }
                ?: requestPokemonUpdate(id)
        }
    }

    override suspend fun requestPokemonUpdate(id: Int): PokemonResponse {
        return withContext(Dispatchers.IO) {
            try {
                Timber.d("retrofit read pokemon id:  $id")
                val response = api.getPokemon(id).execute()
                when (response.isSuccessful) {

                    false -> {
                        Timber.e("pokemonData error response: $response")
                        PokemonResponse.Fail(Exception("Call unsuccessful response: $response"))
                    }

                    true -> {
                        val pokemonData = response.body()!!

                        Timber.d("pokemonData succes  $pokemonData")

                        pokemonDao.insertPokemonData(
                            pokemonData.toEntity(id)
                        )

                        pokemonDao.getPokemon(id)?.toPokemon()
                            ?.let {
                                PokemonResponse.Success(it)
                            }
                            ?: PokemonResponse.Fail(Exception("error getting updated pokemon from database"))

                    }
                }
            } catch (e: Exception) {
                PokemonResponse.Fail(e)
            }
        }

    }
}


