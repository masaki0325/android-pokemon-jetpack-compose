package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.data.paging.PokemonPagingSource
import jp.android.pokemon.domain.usecase.GetPokemonListUseCase
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {

    val pokemonPagingFlow = Pager(PagingConfig(
        pageSize = 20,
        prefetchDistance = 5,
        initialLoadSize = 20,
    )) {
        PokemonPagingSource(getPokemonListUseCase)
    }.flow.cachedIn(viewModelScope)
}


