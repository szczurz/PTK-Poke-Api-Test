package com.pkurkowski.pokeapi.domain

class PokemonSprites(
    backDefault: String?,
    backShiny: String?,
    frontDefault: String?,
    frontShiny: String?,
    backFemale: String?,
    backShinyFemale: String?,
    frontFemale: String?,
    frontShinyFemale: String?
) {

    val map: Map<Triple<Side, Gender, Style>, String>

    init {
        val tempMap = mutableMapOf<Triple<Side, Gender, Style>, String>()

        backDefault?.let { tempMap.put(Triple(Side.Back, Gender.Male, Style.Regular), it) }
        backShiny?.let { tempMap.put(Triple(Side.Back, Gender.Male, Style.Shiny), it) }
        frontDefault?.let { tempMap.put(Triple(Side.Front, Gender.Male, Style.Regular), it) }
        frontShiny?.let { tempMap.put(Triple(Side.Front, Gender.Male, Style.Shiny), it) }
        backFemale?.let { tempMap.put(Triple(Side.Back, Gender.Female, Style.Regular), it) }
        backShinyFemale?.let { tempMap.put(Triple(Side.Back, Gender.Female, Style.Shiny), it) }
        frontFemale?.let { tempMap.put(Triple(Side.Front, Gender.Female, Style.Regular), it) }
        frontShinyFemale?.let { tempMap.put(Triple(Side.Front, Gender.Female, Style.Shiny), it) }

        map = tempMap.toMap()

    }


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
