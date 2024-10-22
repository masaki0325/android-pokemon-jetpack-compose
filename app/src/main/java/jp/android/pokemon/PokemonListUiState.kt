package jp.android.pokemon

sealed class PokemonListUiState {
    data object Loading : PokemonListUiState()
    data object PaginationLoading : PokemonListUiState()
    data object Success : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}
