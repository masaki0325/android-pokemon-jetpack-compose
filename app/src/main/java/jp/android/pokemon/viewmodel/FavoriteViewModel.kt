package jp.android.pokemon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.usecase.AddFavoriteUseCase
import jp.android.pokemon.domain.usecase.GetAllFavoritesUseCase
import jp.android.pokemon.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    val pokemonList = getAllFavoritesUseCase.invoke().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


    fun addToFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            addFavoriteUseCase.invoke(pokemon)
        }
    }

    fun removeFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            removeFavoriteUseCase.invoke(pokemon)
        }
    }
}