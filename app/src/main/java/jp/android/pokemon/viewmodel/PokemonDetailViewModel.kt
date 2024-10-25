package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.domain.usecase.GetPokemonDetailsUseCase
import jp.android.pokemon.ui.state.PokemonDetailUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    fun fetchPokemonDetails(pokemonId: String) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            delay(1000)
            try {
                val details = getPokemonDetailsUseCase(pokemonId)
                _uiState.value = PokemonDetailUiState.Success(details)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}