package jp.android.pokemon.domain.model

data class Pokemon (
    val name: String,
    val url: String,
    val isFavorite: Boolean = false
) {
    // URLからポケモンIDを抽出するプロパティ
    val pokemonId: String
        get() = url.split("/").dropLast(1).last()
}
