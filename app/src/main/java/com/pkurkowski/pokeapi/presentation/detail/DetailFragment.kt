package com.pkurkowski.pokeapi.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pkurkowski.pokeapi.R
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


        onStates(viewModel) {
            when(it) {
                is DetailViewState.LoadingNoData -> renderLoading(it)
                is DetailViewState.Exception -> renderException(it)
                is DetailViewState.ShowData -> renderData(it)
            }
        }

    }

    private fun renderLoading(data: DetailViewState.LoadingNoData) {
        progressBar.isVisible = true
        nameTextView.text = ""
    }

    private fun renderException(data: DetailViewState.Exception) {
        progressBar.isVisible = false
        nameTextView.text = " Exception happen: ${data.exception}"
    }

    private fun renderData(data: DetailViewState.ShowData) {
        progressBar.isVisible = false
        nameTextView.text = data.pokemon.name
    }
}