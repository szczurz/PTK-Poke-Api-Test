package com.pkurkowski.pokeapi.application

import androidx.paging.PagingSource
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonsResponse

class PokemonPagingSource(private val repository: PokemonRepository) : PagingSource<Int, Pokemon>() {

    companion object {
        const val pokemonsPerPage = 50
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val pokemonIndex = params.key ?: 0

        val pokemonToLoad =
            if (pokemonIndex >= 0) pokemonsPerPage else pokemonsPerPage.plus(pokemonIndex)

        val response = repository.getPokemonList(
            pokemonToLoad,
            pokemonIndex.coerceAtLeast(0)
        )
        return when (response) {
            is PokemonsResponse.Success -> {
                val prevKey = if (pokemonIndex <= 0) null else pokemonIndex - pokemonsPerPage
                val nextKey = if (response.hasNext) pokemonIndex + pokemonsPerPage else null
                LoadResult.Page(
                    data = response.data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            is PokemonsResponse.Fail -> LoadResult.Error(
                response.reason
            )
        }

    }
}