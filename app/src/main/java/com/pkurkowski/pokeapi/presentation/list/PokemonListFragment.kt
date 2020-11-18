package com.pkurkowski.pokeapi.presentation.list

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
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
        PokemonAdapter(::onPokemonClicked, viewModel.updatePokemonChannel).apply {
            this.withLoadStateFooter(PokemonLoadStateAdapter(this::retry))
        }
    }

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.e("onCreate")

        viewModel.startInitialState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext())
        
        errorTextView.setOnClickListener { adapter.retry() }
        errorImageView.setOnClickListener { adapter.retry() }
        pokemonRecyclerView.adapter = adapter
        pokemonRecyclerView.layoutManager = layoutManager

        onStates(viewModel) { state ->
            if (state is PokemonListState.InitialFlowAssign) {
                lifecycleScope.launch {
                    adapter.submitData(state.data)
                }
                restoreLinearLayoutManagerPositions(savedInstanceState)
            }
            progressBar.isInvisible = state !is PokemonListState.LoadingInitialData
            setErrorViewsInvisibility(state !is PokemonListState.ErrorAndEmptyData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateEnum.collectLatest {
                viewModel.listStateChanged(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_LAYOUT_MANAGER, layoutManager.onSaveInstanceState())
    }

    private fun restoreLinearLayoutManagerPositions(savedInstanceState: Bundle?) {
        savedInstanceState
            ?.getParcelable<LinearLayoutManager.SavedState>(SAVED_LAYOUT_MANAGER)
            ?.let {
                layoutManager.onRestoreInstanceState(it)
            }
    }

    private fun setErrorViewsInvisibility(invisible: Boolean) {
        errorTextView.isInvisible = invisible
        errorImageView.isInvisible = invisible
    }

    private fun onPokemonClicked(pokemon: Pokemon) {

        pokemon.pokemonId?.let { viewModel.pokemonClicked(it) }

//        pokemon.pokemonId?.let {
//            findNavController().navigate(
//                PokemonListFragmentDirections.showDetail(it)
//            )
//        }
    }

    companion object {
        const val SAVED_LAYOUT_MANAGER = "SAVED_LAYOUT_MANAGER"
    }

}