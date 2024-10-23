package jp.android.pokemon.domain.usecase

import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.repository.FavoritePokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFavoritesUseCase @Inject constructor(
    private val repository: FavoritePokemonRepository
) {
    fun invoke(): Flow<List<FavoritePokemon>> {
        return repository.getAllFavorites()
    }
}