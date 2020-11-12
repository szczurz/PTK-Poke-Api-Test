package com.pkurkowski.pokeapi.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkurkowski.pokeapi.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonListActivity: AppCompatActivity()  {

    private val viewModel: PokemonListViewModel by viewModel()

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.pokemonRecyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)

        val adapter = PokemonAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.pagingFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

}