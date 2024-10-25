package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.usecase.AddFavoriteUseCase
import jp.android.pokemon.domain.usecase.CheckFavoriteStatusUseCase
import jp.android.pokemon.domain.usecase.GetAllFavoritesUseCase
import jp.android.pokemon.domain.usecase.RemoveFavoriteByIdUseCase
import jp.android.pokemon.domain.usecase.RemoveFavoriteUseCase
import jp.android.pokemon.ui.state.FavoriteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val removeFavoriteByIdUseCase: RemoveFavoriteByIdUseCase,
    private val checkFavoriteStatusUseCase: CheckFavoriteStatusUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoriteUiState> = getAllFavoritesUseCase.invoke()
        .map { favorites ->
            if (favorites.isEmpty()) {
                FavoriteUiState.Empty
            } else {
                FavoriteUiState.Success(favorites)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            FavoriteUiState.Loading // 初期状態をLoadingに設定
        )

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun checkIfFavorite(pokemonId: Int) {
        viewModelScope.launch {
            _isFavorite.value = checkFavoriteStatusUseCase.invoke(pokemonId)
        }
    }

    fun addToFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            addFavoriteUseCase.invoke(pokemon)
            _isFavorite.value = true
        }
    }

    fun removeFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            removeFavoriteUseCase.invoke(pokemon)
            _isFavorite.value = false
        }
    }

    fun removeFavoriteByPokemonId(pokemonId: Int) {
        viewModelScope.launch {
            removeFavoriteByIdUseCase.invoke(pokemonId)
            _isFavorite.value = false
        }
    }
}