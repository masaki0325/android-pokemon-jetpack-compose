package jp.android.pokemon.ui.state

import jp.android.pokemon.domain.model.PokemonDetails

sealed class PokemonDetailUiState {
    data object Loading : PokemonDetailUiState()
    data class Success(val details: PokemonDetails) : PokemonDetailUiState()
    data class Error(val message: String) : PokemonDetailUiState()
}