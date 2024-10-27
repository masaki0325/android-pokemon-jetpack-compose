package jp.android.pokemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import jp.android.pokemon.domain.model.Pokemon
import jp.android.pokemon.ui.components.ErrorView
import jp.android.pokemon.ui.components.LoadingView
import jp.android.pokemon.ui.components.PokemonSkeletonItem
import jp.android.pokemon.ui.theme.PokemonTheme
import jp.android.pokemon.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val pagingItems = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()

    val isRefreshing = pagingItems.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { pagingItems.refresh() }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ポケモン") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            when (pagingItems.loadState.refresh) {
                // 初回ローディング中
                is LoadState.Loading -> {
                    // ロード中はスケルトンビューを表示
                    LazyColumn {
                        items(10) {
                            PokemonSkeletonItem()
                        }
                    }
                }
                // 初回ローディング中にエラーが発生した場合
                is LoadState.Error -> {
                    val error = pagingItems.loadState.refresh as LoadState.Error
                    val message = error.error.localizedMessage ?: "エラーが発生しました"
                    ErrorView(message)
                }
                is LoadState.NotLoading -> {
                    LazyColumn {
                        items(pagingItems.itemCount) { index ->
                            val pokemon = pagingItems[index]
                            pokemon?.let {
                                PokemonListItem(
                                    pokemon = it,
                                    onItemClicked = {
                                        navController.navigate("pokemonDetail/${it.pokemonId}")
                                    }
                                )
                            }
                        }
                        // ページングのロード中にフッターにインジケータを表示
                        if (pagingItems.loadState.append == LoadState.Loading) {
                            item {
                                PagingLoadingView()
                            }
                        }
                        // ページングのエラー時にフッターにエラーメッセージを表示
                        if (pagingItems.loadState.append is LoadState.Error) {
                            val appendError = pagingItems.loadState.append as LoadState.Error
                            val message = appendError.error.localizedMessage ?: "エラーが発生しました"
                            item {
                                ErrorView(message)
                            }
                        }
                    }
                    // Pull-to-Refreshのインジケーターを追加
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

@Composable
private fun PagingLoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun PagingLoadingViewPreview() {
    PokemonTheme {
        PagingLoadingView()
    }
}

@Composable
private fun PokemonListItem(
    pokemon: Pokemon,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
    ) {
        Row {
            Text(
                text = pokemon.pokemonId,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = pokemon.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonItemPreview() {
    PokemonTheme {
        PokemonListItem(
            pokemon = Pokemon(
                "Pikachu",
                "https://pokeapi.co/api/v2/pokemon/25/"
            ),
            onItemClicked = {}
        )
    }
}

