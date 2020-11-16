package com.pkurkowski.pokeapi.application

import androidx.paging.PagingSource
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonsResponse
import timber.log.Timber

class PokemonPagingSource(val repository: PokemonRepository) : PagingSource<Int, Pokemon>() {

    companion object {
        const val pokemonsPerPage = 50
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val page = params.key ?: 0
        val response = repository.getPokemonList(
            pokemonsPerPage,
            pokemonsPerPage * page
        )

        return when (response) {
            is PokemonsResponse.Success -> LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = if (response.hasNext) page + 1 else null
            )
            is PokemonsResponse.Fail -> LoadResult.Error(
                response.reason
            )
        }

    }

}