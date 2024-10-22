package jp.android.pokemon

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoritePokemon::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}