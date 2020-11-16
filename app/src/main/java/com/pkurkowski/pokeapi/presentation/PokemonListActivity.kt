package com.pkurkowski.pokeapi.presentation

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pkurkowski.pokeapi.R
import com.pkurkowski.pokeapi.presentation.adapter.PokemonAdapter
import com.pkurkowski.pokeapi.presentation.adapter.PokemonLoadStateAdapter
import io.uniflow.androidx.flow.onStates
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class PokemonListActivity : AppCompatActivity() {

    private val viewModel: PokemonListViewModel by viewModel()
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.pokemonRecyclerView) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val errorTextView: TextView by lazy { findViewById(R.id.errorTextView) }
    private val errorImageView: ImageView by lazy { findViewById(R.id.errorImageView) }

    private val adapter = PokemonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)

        errorTextView.setOnClickListener { adapter.retry() }
        errorImageView.setOnClickListener { adapter.retry() }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter.withLoadStateFooter(PokemonLoadStateAdapter(adapter::retry))
        recyclerView.layoutManager = layoutManager


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //viewModel.requestUpdate(layoutManager.findFirstCompletelyVisibleItemPosition())
                }

            }
        })

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.map {
                val state = it.refresh
                if (adapter.isEmpty()) {
                    when (state) {
                        is LoadState.NotLoading -> AdapterLoadStateEnum.EMPTY_DOING_NOTHING
                        is LoadState.Loading -> AdapterLoadStateEnum.EMPTY_LOADING
                        is LoadState.Error -> AdapterLoadStateEnum.EMPTY_ERROR
                    }
                } else {
                    AdapterLoadStateEnum.FILLED_ADAPTER_WORKING
                }


            }.collect {
                viewModel.listStateChanged(it)
            }
        }

        onStates(viewModel) { state ->
            Timber.d("state: $state")

            if (state is PokemonListState.InitialFlowAssign) {
                lifecycleScope.launch {
                    state.data.collectLatest {
                        adapter.submitData(it)
                    }
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

    private fun PokemonAdapter.isEmpty() = this.itemCount == 0


}