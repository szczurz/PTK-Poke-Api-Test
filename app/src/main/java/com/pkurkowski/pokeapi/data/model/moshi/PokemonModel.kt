package com.pkurkowski.pokeapi.data.model.moshi

import android.net.Uri
import com.pkurkowski.pokeapi.data.model.room.PokemonEntity
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData

data class PokemonModel(
    val url: String,
    val name: String
)

fun PokemonModel.toEntity(index: Int): PokemonEntity {
    val remoteId = Uri.parse(url).lastPathSegment?.toInt()
    return PokemonEntity(
        pokemonIndex = index,
        pokemonId = remoteId,
        name = name.toUpperCase()
    )
}

fun PokemonModel.toPokemon(index: Int): Pokemon {
    val remoteId = Uri.parse(url).lastPathSegment?.toInt()
    return Pokemon(
        index = index,
        pokemonId = remoteId,
        name = name.toUpperCase(),
        data = PokemonData.Empty
    )
}