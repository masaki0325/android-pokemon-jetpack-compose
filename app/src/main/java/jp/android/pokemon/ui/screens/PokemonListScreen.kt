package jp.android.pokemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jp.android.pokemon.domain.model.Pokemon
import jp.android.pokemon.ui.state.PokemonListUiState
import jp.android.pokemon.ui.theme.PokemonTheme
import jp.android.pokemon.viewmodel.PokemonViewModel

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = { PokemonTopAppBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            when (uiState) {
                PokemonListUiState.Loading -> LoadingView()
                PokemonListUiState.Success, PokemonListUiState.PaginationLoading -> {
                    PokemonList(
                        pokemonList = pokemonList,
                        onItemClicked = { pokemonId ->
                            navController.navigate("pokemonDetail/$pokemonId")
                        },
                        isLoading = isLoading,
                        onEndReached = { viewModel.loadNextPage() },
                        listState = listState,
                        showPaginationLoading = uiState is PokemonListUiState.PaginationLoading
                    )
                }
                is PokemonListUiState.Error -> {
                    ErrorView((uiState as PokemonListUiState.Error).message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTopAppBar() {
    TopAppBar(title = { Text("ポケモン") })
}

@Preview(showBackground = true)
@Composable
private fun PokemonTopAppBarPreview() {
    PokemonTheme {
        PokemonTopAppBar()
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingViewPreview() {
    PokemonTheme {
        LoadingView()
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    PokemonTheme {
        ErrorView(errorMessage = "エラーが発生しました")
    }
}

@Composable
fun PokemonList(
    pokemonList: List<Pokemon>,
    onItemClicked: (String) -> Unit,
    isLoading: Boolean,
    onEndReached: () -> Unit,
    listState: LazyListState,
    showPaginationLoading: Boolean
) {
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(pokemonList) { pokemon ->
            PokemonItem(pokemon, onItemClicked = { onItemClicked(pokemon.pokemonId) })
        }
        if (showPaginationLoading) {
            item {
                LoadingView(modifier = Modifier.fillMaxWidth().padding(16.dp))
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == pokemonList.size - 1 && !isLoading) {
                    onEndReached()
                }
            }
    }
}
@Preview(showBackground = true)
@Composable
private fun PokemonListPreview() {
    PokemonTheme {
        PokemonList(
            pokemonList = listOf(
                Pokemon("Pikachu", "https://pokeapi.co/api/v2/pokemon/25/"),
                Pokemon("Charmander", "https://pokeapi.co/api/v2/pokemon/4/"),
                Pokemon("Squirtle", "https://pokeapi.co/api/v2/pokemon/7/")
            ),
            onItemClicked = {},
            isLoading = false,
            onEndReached = {},
            listState = rememberLazyListState(),
            showPaginationLoading = true
        )
    }
}


@Composable
fun PokemonItem(
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
        PokemonItem(
            pokemon = Pokemon("Pikachu", "https://pokeapi.co/api/v2/pokemon/25/"),
            onItemClicked = {}
        )
    }
}