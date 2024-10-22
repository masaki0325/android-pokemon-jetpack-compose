package jp.android.pokemon

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritePokemonRepository @Inject constructor(private val dao: FavoritePokemonDao) {

    fun getAllFavorites(): Flow<List<FavoritePokemon>> = dao.getAllFavorites()

    suspend fun insert(favoritePokemon: FavoritePokemon) {
        dao.insert(favoritePokemon)
    }

    suspend fun delete(favoritePokemon: FavoritePokemon) {
        dao.delete(favoritePokemon)
    }
}