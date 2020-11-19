package com.pkurkowski.pokeapi.presentation.list

import com.pkurkowski.pokeapi.presentation.list.adapter.UpdateStatus
import io.uniflow.core.flow.data.UIEvent

sealed class PokemonListEvent : UIEvent() {
    data class PokemonSelectedEvent(val pokemonId: Int) : PokemonListEvent()
    data class PokemonUpdatedEvent(val pokemonIndex: Int, val status: UpdateStatus) :
        PokemonListEvent()
}