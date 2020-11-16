package com.pkurkowski.pokeapi.application.di

import com.pkurkowski.pokeapi.data.model.retrofit.PokeApiInterface
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val remoteDataSourceModule = module {

    //OkHttpClient
    single { OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .build() }


    //Get PokeApi
    single { Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(get())
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create(PokeApiInterface::class.java)
    }
}