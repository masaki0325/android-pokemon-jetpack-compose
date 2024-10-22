package jp.android.pokemon

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class PokemonResponse (
    var results: List<Pokemon> = emptyList()
)

data class Pokemon (
    val name: String,
    val url: String,
    val isFavorite: Boolean = false
) {
    // URLからポケモンIDを抽出するプロパティ
    val pokemonId: String
        get() = url.split("/").dropLast(1).last()
}

data class PokemonDetails(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites
)

data class Sprites(
    val front_default: String
)

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