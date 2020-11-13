package com.pkurkowski.pokeapi.application.di

import androidx.room.Room
import com.pkurkowski.pokeapi.data.model.room.PokemonDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDataSourceModule = module {

    single {
        Room.databaseBuilder(androidApplication(), PokemonDatabase::class.java, "pokemonDataBase")
            .build()
    }

    single { get<PokemonDatabase>().pokemonDao() }
}