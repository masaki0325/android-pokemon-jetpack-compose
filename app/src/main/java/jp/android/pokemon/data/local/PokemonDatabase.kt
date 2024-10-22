package jp.android.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.android.pokemon.data.local.FavoritePokemonDao
import jp.android.pokemon.data.model.FavoritePokemon

@Database(entities = [FavoritePokemon::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}