package com.pkurkowski.pokeapi.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.Pokemon
import com.pkurkowski.pokeapi.domain.PokemonData
import com.pkurkowski.pokeapi.domain.PokemonSprites
import com.pkurkowski.pokeapi.domain.getSBasicDataOrNull
import io.uniflow.androidx.flow.onStates
import kotlinx.android.synthetic.main.fragment_pokemon_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment() : Fragment() {

    private val viewModel: DetailViewMode by viewModel()
    private val arguments: DetailFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData(arguments.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spritesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        onStates(viewModel) {
            when (it) {
                is DetailViewState.LoadingNoData -> renderLoading(it)
                is DetailViewState.Exception -> renderException(it)
                is DetailViewState.ShowData -> renderData(it)
            }
        }

    }

    private fun renderLoading(state: DetailViewState.LoadingNoData) {
        progressBar.isVisible = true
        spritesRecyclerView.isVisible = false
        descriptionTextView.isVisible = false

        nameTextView.text = ""
    }

    private fun renderException(state: DetailViewState.Exception) {
        progressBar.isVisible = false
        spritesRecyclerView.isVisible = false
        descriptionTextView.isVisible = true

        descriptionTextView.text = " Exception happen: ${state.exception}"
        nameTextView.text = ""
    }

    private fun renderData(state: DetailViewState.ShowData) {
        progressBar.isVisible = false
        spritesRecyclerView.isVisible = true
        descriptionTextView.isVisible = true

        nameTextView.text = state.pokemon.name

        when (val basicData = state.pokemon.getSBasicDataOrNull()) {
            null -> {
                spritesRecyclerView.isVisible = false
                descriptionTextView.text =
                    resources.getString(R.string.pokemon_data_missing_message)
            }
            else -> {
                spritesRecyclerView.isVisible = true
                spritesRecyclerView.adapter = SpritesAdapter(basicData.sprites.regularSprites)

                descriptionTextView.text = resources.getString(
                    R.string.pokemon_data_description,
                    basicData.baseExperience,
                    basicData.height,
                    basicData.weight,
                    basicData.isDefault,
                )
            }
        }
    }
}