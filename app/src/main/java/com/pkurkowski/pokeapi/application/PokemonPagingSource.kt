package com.pkurkowski.pokeapi.application

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonsResponse
import timber.log.Timber

class PokemonPagingSource(val repository: PokemonRepository) : PagingSource<Int, Pokemon>() {

    companion object {
        const val pokemonsPerPage = 50
        const val anchorPointBuffer = 20
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

                //if(pokemonIndex < pokemonsPerPage) null else pokemonIndex - pokemonsPerPage

                val prevKey = if (pokemonIndex <= 0) null
                else pokemonIndex - pokemonsPerPage

                val nextKey = if (response.hasNext) pokemonIndex + pokemonsPerPage else null

                Timber.tag("ZZZZZ").d("PokemonPagingSource load page: $pokemonIndex")
                Timber.tag("ZZZZZ").d("prevKey: $prevKey nextKey: $nextKey")


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

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {

        val result = state.anchorPosition?.minus(anchorPointBuffer)

        var closest: Int? = null
        state.anchorPosition?.let {
                closest = state.closestItemToPosition(it)?.index?.minus(anchorPointBuffer)
        }



        //val closest = state.getClosestItemToPosition(anchorPosition)?.id

        Timber.tag("ZZZZZ")
            .d("PokemonPagingSource getRefreshKey anchorPosition: ${state.anchorPosition}  closest: $closest")


        return closest
    }

    override val keyReuseSupported: Boolean
        get() = true
}