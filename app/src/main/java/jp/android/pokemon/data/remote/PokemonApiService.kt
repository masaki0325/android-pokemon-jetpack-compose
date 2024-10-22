package jp.android.pokemon.data.remote

import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    // ポケモンのリストを取得するエンドポイント
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): PokemonResponse

    // ポケモンの詳細情報を取得するエンドポイント
    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(
        @Path("id") id: String
    ): PokemonDetails
}