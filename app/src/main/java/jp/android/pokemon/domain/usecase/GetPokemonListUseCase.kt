package jp.android.pokemon.domain.usecase

import jp.android.pokemon.domain.model.PokemonResponse
import jp.android.pokemon.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(limit: Int, offset: Int): PokemonResponse {
        return repository.getPokemonList(limit, offset)
    }
}