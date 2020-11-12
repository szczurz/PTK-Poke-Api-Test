package com.pkurkowski.pokeapi.domain

interface PokemonSprites {

    fun getSpriteUrl(
        side:Side = Side.Front,
        gender:Gender = Gender.Male,
        style:Style = Style.Shiny,
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
}
