package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.model.Pokemon
import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.repository.FavoritePokemonRepository
import jp.android.pokemon.ui.state.PokemonListUiState
import jp.android.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val favoriteRepository: FavoritePokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails: StateFlow<PokemonDetails?> = _pokemonDetails

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState

    private var currentPage = 1 // 現在のページ番号を管理
    private val pageSize = 20 // 1ページあたりの件数

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val favoritePokemonList = favoriteRepository.getAllFavorites().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList() // 初期値として空のリストを設定
    )

    init {
        fetchPokemonList(isInitialLoad = true)
    }

    private fun fetchPokemonList(isInitialLoad: Boolean = false) {
        viewModelScope.launch {
            if (isInitialLoad) {
                _uiState.value = PokemonListUiState.Loading // 最初のロード時は全画面ローディング
            } else {
                _uiState.value = PokemonListUiState.PaginationLoading // ページネーション時のローディング
            }

            _isLoading.value = true

            delay(1000)
            try {
                val response = repository.getPokemonList(page = currentPage, limit = pageSize)
                _pokemonList.value = _pokemonList.value + response.results
                _uiState.value = PokemonListUiState.Success
                currentPage++
            } catch (e: Exception) {
                _uiState.value = PokemonListUiState.Error(e.message ?: "An unexpected error occurred!")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            val details = repository.getPokemonDetails(pokemonId)
            _pokemonDetails.value = details
        }
    }

    fun loadNextPage() {
        if (_isLoading.value.not()) {
            fetchPokemonList(isInitialLoad = false)
        }
    }

    fun addToFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            favoriteRepository.insert(favoritePokemon = pokemon)
        }
    }

    fun removeFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            favoriteRepository.delete(favoritePokemon = pokemon)
        }
    }
}