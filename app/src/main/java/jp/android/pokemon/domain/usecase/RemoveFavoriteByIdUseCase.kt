package jp.android.pokemon.domain.usecase

import jp.android.pokemon.domain.repository.FavoritePokemonRepository
import javax.inject.Inject

class RemoveFavoriteByIdUseCase @Inject constructor(
    private val repository: FavoritePokemonRepository
) {
    suspend fun invoke(pokemonId: Int) {
        repository.deleteById(pokemonId)
    }
}