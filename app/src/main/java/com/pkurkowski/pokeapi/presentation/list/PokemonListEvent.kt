package com.pkurkowski.pokeapi.presentation.list

import com.pkurkowski.pokeapi.domain.PokemonData
import io.uniflow.core.flow.data.UIEvent

sealed class PokemonListEvent : UIEvent() {
    data class PokemonSelectedEvent(val pokemonId: Int) : PokemonListEvent()
    data class PokemonUpdatedEvent(val pokemonIndex: Int, val data: PokemonData.PokemonBasicData) :
        PokemonListEvent()
}