package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.model.Pokemon
import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.ui.state.PokemonListUiState
import jp.android.pokemon.domain.usecase.AddFavoriteUseCase
import jp.android.pokemon.domain.usecase.GetAllFavoritesUseCase
import jp.android.pokemon.domain.usecase.GetPokemonDetailsUseCase
import jp.android.pokemon.domain.usecase.GetPokemonListUseCase
import jp.android.pokemon.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoadType {
    INITIAL, PAGINATION
}

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails: StateFlow<PokemonDetails?> = _pokemonDetails

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState

    private var offset = 0

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchPokemonList(LoadType.INITIAL)
    }

    private fun fetchPokemonList(loadType: LoadType) {
        viewModelScope.launch {
            when (loadType) {
                LoadType.INITIAL -> _uiState.value = PokemonListUiState.Loading
                LoadType.PAGINATION -> _uiState.value = PokemonListUiState.PaginationLoading
            }

            _isLoading.value = true
            delay(1000)
            try {
                val response = getPokemonListUseCase.invoke(20, offset)
                _pokemonList.value += response.results
                _uiState.value = PokemonListUiState.Success
                offset += 20
            } catch (e: Exception) {
                _uiState.value = PokemonListUiState.Error(e.message ?: "An unexpected error occurred!")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            val details = getPokemonDetailsUseCase(pokemonId)
            _pokemonDetails.value = details
        }
    }

    fun loadNextPage() {
        if (_isLoading.value.not()) {
            fetchPokemonList(LoadType.PAGINATION)
        }
    }
}