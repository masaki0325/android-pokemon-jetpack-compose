package jp.android.pokemon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import jp.android.pokemon.ui.theme.PokemonTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ポケモン") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (uiState) {
                PokemonListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                PokemonListUiState.Success, PokemonListUiState.PaginationLoading -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(pokemonList) { pokemon ->
                            PokemonItem(pokemon, onItemClicked = {
                                navController.navigate("pokemonDetail/${pokemon.pokemonId}")
                            })
                        }
                        if (uiState is PokemonListUiState.PaginationLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .collect { lastVisibleIndex ->
                                if (lastVisibleIndex == pokemonList.size - 1 && !isLoading) {
                                    viewModel.loadNextPage()
                                }
                            }
                    }
                }
                is PokemonListUiState.Error -> {
                    val errorMessage = (uiState as PokemonListUiState.Error).message
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onItemClicked: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onItemClicked() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
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