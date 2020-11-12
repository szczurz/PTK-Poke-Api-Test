package com.pkurkowski.pokeapi.data.repository

import com.pkurkowski.pokeapi.data.model.retrofit.PokeApiInterface
import com.pkurkowski.pokeapi.data.model.room.PokemonDao
import com.pkurkowski.pokeapi.data.model.room.PokemonDataDao
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepositoryImp(
    val api: PokeApiInterface,
    val pokemonDao: PokemonDao,
    val pokemonDataDao: PokemonDataDao
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): PokemonResponse {
        return withContext(Dispatchers.IO) {
            api.getPokemons(limit, offset).execute().body()!!.let {
                PokemonResponse.Success(
                    hasNext = it.results.size + offset < it.count,
                    data = it.results.mapIndexed { index, pokemonModel ->
                        Pokemon(index + offset, pokemonModel.name, PokemonData.Empty)
                    }
                )
            }
        }
    }
}


