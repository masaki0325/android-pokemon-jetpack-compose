package jp.android.pokemon.domain.usecase

import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.repository.FavoritePokemonRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: FavoritePokemonRepository
) {
    suspend fun invoke(pokemon: FavoritePokemon) {
        repository.insert(pokemon)
    }
}