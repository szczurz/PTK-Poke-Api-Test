package com.pkurkowski.pokeapi.application.di

import com.pkurkowski.pokeapi.BuildConfig
import com.pkurkowski.pokeapi.data.model.retrofit.PokeApiInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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


    single {
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    }


    //Get PokeApi
    single { Retrofit.Builder()
        .baseUrl(BuildConfig.POKE_API_URL)
        .client(get())
        .addConverterFactory(MoshiConverterFactory.create(get()))
        .build().create(PokeApiInterface::class.java)
    }
}