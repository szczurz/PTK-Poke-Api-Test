package com.pkurkowski.pokeapi.domain

data class PokemonSprites(
    val regularSprites: Map<SpriteDescription, String>,
    val dreamWordSprites: Map<SpriteDescription, String>,
    val officialArtWork: Pair<SpriteDescription, String>?
)

enum class Side {
    Front, Back
}

enum class Gender {
    Male, Female
}

enum class Style {
    Regular, Shiny
}

data class SpriteDescription(val side: Side, val gender: Gender, val style: Style)
