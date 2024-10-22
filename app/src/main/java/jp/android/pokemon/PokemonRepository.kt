package jp.android.pokemon

import javax.inject.Inject

class PokemonRepository @Inject constructor(private val pokemonApiService: PokemonApiService) {

    suspend fun getPokemonList(page: Int, limit: Int = 20): PokemonResponse {
        val offset = (page - 1) * limit  // ページに応じてオフセットを計算
        return pokemonApiService.getPokemonList(limit, offset)
    }

    suspend fun getPokemonDetails(pokemonId: String): PokemonDetails {
        return pokemonApiService.getPokemonDetails(pokemonId)
    }
}