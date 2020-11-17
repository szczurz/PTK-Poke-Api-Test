package com.pkurkowski.pokeapi.domain

class PokemonSprites(val map: Map<SpriteDescription, String>)

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
