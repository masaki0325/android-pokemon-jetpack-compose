package jp.android.pokemon.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import jp.android.pokemon.domain.model.Pokemon
import jp.android.pokemon.domain.usecase.GetPokemonListUseCase

class PokemonPagingSource(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val currentPage = params.key ?: 0
            val response = getPokemonListUseCase(params.loadSize, currentPage * params.loadSize)
            LoadResult.Page(
                data = response.results,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (response.results.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // データを破棄した後、再取得する（再びloadを呼び出す）際に使用するkeyを設定する。
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}