package com.pkurkowski.pokeapi.presentation.detail

import androidx.lifecycle.viewModelScope
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.domain.PokemonResponse
import io.uniflow.androidx.flow.AndroidDataFlow
import kotlinx.coroutines.launch

class DetailViewMode(private val repository: PokemonRepository) : AndroidDataFlow() {

    fun getData(id: Int) = action {
        setState { DetailViewState.LoadingNoData(id) }
            val data = repository.requestPokemon(id)
                setState {
                    when(data) {
                        is PokemonResponse.Success -> DetailViewState.ShowData(data.data)
                        is PokemonResponse.Fail -> DetailViewState.Exception(id, data.reason)
                    }
                }
        }

}