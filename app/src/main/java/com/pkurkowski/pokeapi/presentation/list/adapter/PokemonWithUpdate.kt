package com.pkurkowski.pokeapi.presentation.list.adapter

import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData

data class PokemonWithUpdate(val original: Pokemon, var update: PokemonData.PokemonBasicData? = null)