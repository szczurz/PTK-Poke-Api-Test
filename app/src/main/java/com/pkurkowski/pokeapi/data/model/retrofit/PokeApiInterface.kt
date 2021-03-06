package com.pkurkowski.pokeapi.data.model.retrofit

import com.pkurkowski.pokeapi.data.model.moshi.PokemonDataModel
import com.pkurkowski.pokeapi.data.model.moshi.PokemonsResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiInterface {

    @GET("pokemon")
    fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Call<PokemonsResponseModel>


    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<PokemonDataModel>
}