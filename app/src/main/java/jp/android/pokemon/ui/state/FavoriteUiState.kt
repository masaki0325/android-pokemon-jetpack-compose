package jp.android.pokemon.ui.state

import jp.android.pokemon.data.model.FavoritePokemon

sealed class FavoriteUiState {
    data object Loading : FavoriteUiState()
    data class Success(val favoriteList: List<FavoritePokemon>) : FavoriteUiState()
    data object Empty : FavoriteUiState()
}