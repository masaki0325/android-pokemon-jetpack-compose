package jp.android.pokemon.domain.usecase

import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(pokemonId: String): PokemonDetails {
        return repository.getPokemonDetails(pokemonId)
    }
}