package jp.android.pokemon.domain.repository

import jp.android.pokemon.data.remote.PokemonApiService
import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.model.PokemonResponse
import javax.inject.Inject

class PokemonRepository @Inject constructor(private val pokemonApiService: PokemonApiService) {

    suspend fun getPokemonList(limit: Int = 20, offset: Int): PokemonResponse {
        return pokemonApiService.getPokemonList(limit, offset)
    }

    suspend fun getPokemonDetails(pokemonId: String): PokemonDetails {
        return pokemonApiService.getPokemonDetails(pokemonId)
    }
}