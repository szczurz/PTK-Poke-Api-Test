package com.pkurkowski.pokeapi.presentation.detail

import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonResponse
import io.uniflow.androidx.flow.AndroidDataFlow

class DetailViewMode(private val repository: PokemonRepository) : AndroidDataFlow() {

    fun getData(id: Int) = action {
        setState { DetailViewState.LoadingNoData(id) }
            val data = repository.requestPokemon(id)
                setState {
                    when(data) {
                        is PokemonResponse.Success -> DetailViewState.ShowData(data.pokemon)
                        is PokemonResponse.Fail -> DetailViewState.Exception(id, data.reason)
                    }
                }
        }

}