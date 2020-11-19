package com.pkurkowski.pokeapi.presentation.list.adapter

import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData

data class PokemonWithUpdate(val original: Pokemon, var updateStatus: UpdateStatus = UpdateStatus.Empty)

sealed class UpdateStatus {
    object Empty: UpdateStatus()
    object InProgress: UpdateStatus()
    object Error: UpdateStatus()
    data class Updated(val pokemonData: PokemonData.PokemonBasicData): UpdateStatus()
}