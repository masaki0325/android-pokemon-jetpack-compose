package jp.android.pokemon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.ui.state.FavoriteUiState
import jp.android.pokemon.ui.theme.PokemonTheme
import jp.android.pokemon.viewmodel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("お気に入り") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (uiState) {
                is FavoriteUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is FavoriteUiState.Success -> {
                    val list = (uiState as FavoriteUiState.Success).favoriteList
                    FavoritesList(
                        list,
                        onItemClicked = { favoritePokemon ->
                            navController.navigate("pokemonDetail/${favoritePokemon.pokemonId}")
                        },
                        onDelete = { favoritePokemon ->
                            viewModel.removeFavorite(favoritePokemon)
                        }
                    )
                }
                is FavoriteUiState.Empty -> {
                    Text(
                        text = "お気に入りがありません",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesList(
    favoriteList: List<FavoritePokemon>,
    onItemClicked: (FavoritePokemon) -> Unit = {},
    onDelete: (FavoritePokemon) -> Unit = {}
) {
    LazyColumn() {
        items(favoriteList, key = { it.id }) { pokemon ->
            val swipeState = rememberSwipeToDismissBoxState()
            SwipeToDismissBox(
                state = swipeState,
                backgroundContent = {
                    if (swipeState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red)
                                .padding(16.dp),
                        )
                    }
                }
            ) {
                PokemonItem(
                    pokemon = pokemon,
                    onItemClicked = {
                        onItemClicked(pokemon)
                    }
                )
            }
            if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                LaunchedEffect(pokemon) {
                    onDelete(pokemon)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    PokemonTheme {
        FavoritesList(
            listOf(
                FavoritePokemon(
                    1,
                    1,
                    "bulbasaur",
                    "https://pokeapi.co/api/v2/pokemon/1/"
                ),
                FavoritePokemon(
                    2,
                    2,
                    "ivysaur",
                    "https://pokeapi.co/api/v2/pokemon/2/"
                ),
                FavoritePokemon(
                    3,
                    3,
                    "venusaur",
                    "https://pokeapi.co/api/v2/pokemon/3/"
                )
            )
        )
    }
}

@Composable
private fun PokemonItem(
    pokemon: FavoritePokemon,
    onItemClicked: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClicked() }
        ) {
            Text(
                text = pokemon.pokemonId.toString(),
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
            pokemon = FavoritePokemon(
                1,
                1,
                "bulbasaur",
                "https://pokeapi.co/api/v2/pokemon/1/"
            ),
            onItemClicked = {}
        )
    }
}