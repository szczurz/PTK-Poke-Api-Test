package com.pkurkowski.pokeapi.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pkurkowski.pokeapi.application.PokemonPagingSource
import com.pkurkowski.pokeapi.data.model.retrofit.PokeApiInterface
import com.pkurkowski.pokeapi.data.repository.PokemonRepositoryImp
import com.pkurkowski.pokeapi.domain.PokemonRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PokemonListViewModel(
    val handle: SavedStateHandle,
    private val repository: PokemonRepository
) : ViewModel() {


    val pagingFlow = Pager(
        PagingConfig(
            pageSize = PokemonPagingSource.pokemonsPerPage,
            prefetchDistance = PokemonPagingSource.pokemonsPerPage * 2
        )
    ) {
        PokemonPagingSource(repository)
    }.flow.cachedIn(viewModelScope)


}