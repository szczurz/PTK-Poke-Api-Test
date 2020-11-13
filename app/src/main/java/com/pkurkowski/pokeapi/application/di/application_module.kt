package com.pkurkowski.pokeapi.application.di

import com.pkurkowski.pokeapi.data.repository.PokemonRepositoryImp
import com.pkurkowski.pokeapi.domain.PokemonRepository
import com.pkurkowski.pokeapi.presentation.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single<PokemonRepository> {
            PokemonRepositoryImp(
                get(), get()
            )
    }
    viewModel { PokemonListViewModel(get(), get()) }
}