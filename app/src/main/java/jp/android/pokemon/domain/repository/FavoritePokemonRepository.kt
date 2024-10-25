package jp.android.pokemon.domain.repository

import jp.android.pokemon.data.local.FavoritePokemonDao
import jp.android.pokemon.data.model.FavoritePokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritePokemonRepository @Inject constructor(private val dao: FavoritePokemonDao) {

    fun getAllFavorites(): Flow<List<FavoritePokemon>> = dao.getAllFavorites()

    suspend fun isFavorite(pokemonId: Int): Boolean {
        return dao.isFavorite(pokemonId)
    }

    suspend fun insert(favoritePokemon: FavoritePokemon) {
        dao.insert(favoritePokemon)
    }

    suspend fun delete(favoritePokemon: FavoritePokemon) {
        dao.delete(favoritePokemon)
    }

    suspend fun deleteById(pokemonId: Int) {
        dao.deleteByPokemonId(pokemonId)
    }
}