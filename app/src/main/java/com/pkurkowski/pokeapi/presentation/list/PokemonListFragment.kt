package com.pkurkowski.pokeapi.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.presentation.list.adapter.PokemonAdapter
import com.pkurkowski.pokeapi.presentation.list.adapter.PokemonLoadStateAdapter
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {

    private val viewModel: PokemonListViewModel by viewModel()

    private val adapter: PokemonAdapter by lazy {
        PokemonAdapter(::onPokemonClicked).apply {
            this.withLoadStateFooter(PokemonLoadStateAdapter(this::retry))
        }
    }

    init {
        lifecycleScope.launchWhenCreated {
            adapter.loadStateEnum.collectLatest {
                viewModel.listStateChanged(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorTextView.setOnClickListener { adapter.retry() }
        errorImageView.setOnClickListener { adapter.retry() }
        pokemonRecyclerView.adapter = adapter
        pokemonRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        onStates(viewModel) { state ->
            if (state is PokemonListState.InitialFlowAssign) {
                lifecycleScope.launch {
                    adapter.submitData(state.data)
                }
            }
            progressBar.isInvisible = state !is PokemonListState.LoadingInitialData
            setErrorViewsInvisibility(state !is PokemonListState.ErrorAndEmptyData)
        }

    }

    private fun setErrorViewsInvisibility(invisible: Boolean) {
        Timber.d("setErrorViewsInvisibility: $invisible")
        errorTextView.isInvisible = invisible
        errorImageView.isInvisible = invisible
    }

    private fun onPokemonClicked(pokemon: Pokemon) {
        pokemon.pokemonId?.let {
            findNavController().navigate(
                PokemonListFragmentDirections.showDetail(it)
            )
        }
    }

}