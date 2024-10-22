package jp.android.pokemon.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.android.pokemon.data.local.PokemonDatabase
import jp.android.pokemon.data.local.FavoritePokemonDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext appContext: Context): PokemonDatabase {
        return Room.databaseBuilder(
            appContext,
            PokemonDatabase::class.java,
            "pokemon_db"
        ).build()
    }

    @Provides
    fun provideFavoritePokemonDao(database: PokemonDatabase): FavoritePokemonDao {
        return database.favoritePokemonDao()
    }
}