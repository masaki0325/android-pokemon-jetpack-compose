package jp.android.pokemon.domain.usecase

import jp.android.pokemon.domain.repository.FavoritePokemonRepository
import javax.inject.Inject

class CheckFavoriteStatusUseCase @Inject constructor(
    private val repository: FavoritePokemonRepository
) {
    suspend fun invoke(pokemonId: Int) : Boolean {
        return repository.isFavorite(pokemonId)
    }
}