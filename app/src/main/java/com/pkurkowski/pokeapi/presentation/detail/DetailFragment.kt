package com.pkurkowski.pokeapi.presentation.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.domain.*
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
                spritesRecyclerView.adapter =
                    SpritesAdapter(basicData.sprites.map.filter { it.key.source != Source.DreamWorks })
                descriptionTextView.text = resources.getString(
                    R.string.pokemon_data_description,
                    basicData.baseExperience,
                    basicData.height,
                    basicData.weight,
                    basicData.isDefault,
                )

                mainImageView.isVisible = basicData.sprites.map
                    .filter { it.key.source == Source.DreamWorks }
                    .values.firstOrNull()?.let {
                        Uri.parse(it).let {uri ->

                            if(uri == null) {
                                mainImageView.setImageResource(R.drawable.ic_twotone_error_24)
                            } else {
                                GlideToVectorYou
                                    .init()
                                    .with(mainImageView.context)
                                    .setPlaceHolder(R.drawable.ic_baseline_sync_24, R.drawable.ic_twotone_error_24)
                                    .load(uri, mainImageView);
                            }
                        }
                        true
                    } ?: false

            }
        }
    }

}