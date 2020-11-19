package com.pkurkowski.pokeapi.domain

data class PokemonSprites(
    val map: Map<SpriteDescription, String>,
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

enum class Source {
    Regular, DreamWorks, Official
}

data class SpriteDescription(val side: Side, val gender: Gender, val style: Style, val source: Source)
