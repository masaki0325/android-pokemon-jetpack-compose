package jp.android.pokemon.ui.state

sealed class PokemonListUiState {
    data object Loading : PokemonListUiState()
    data object PaginationLoading : PokemonListUiState()
    data object Success : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}
