package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.data.paging.PokemonPagingSource
import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.usecase.GetPokemonDetailsUseCase
import jp.android.pokemon.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
) : ViewModel() {

    val pokemonPagingFlow = Pager(PagingConfig(
        pageSize = 20,
        prefetchDistance = 5,
        initialLoadSize = 20,
    )) {
        PokemonPagingSource(getPokemonListUseCase)
    }.flow.cachedIn(viewModelScope)

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails: StateFlow<PokemonDetails?> = _pokemonDetails

    fun fetchPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            val details = getPokemonDetailsUseCase(pokemonId)
            _pokemonDetails.value = details
        }
    }
}


