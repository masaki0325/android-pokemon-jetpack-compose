package jp.android.pokemon

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.foundation.SwipeToDismissValue
import jp.android.pokemon.ui.theme.PokemonTheme

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val pokemonList by viewModel.favoritePokemonList.collectAsState()
    FavoritesScreen(
        pokemonList,
        onItemClicked = { pokemon ->
        },
        onDelete = { pokemon ->
            viewModel.removeFavorite(pokemon)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    pokemonList: List<FavoritePokemon>,
    onItemClicked: (FavoritePokemon) -> Unit = {},
    onDelete: (FavoritePokemon) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("お気に入り") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(pokemonList, key = { it.id }) { pokemon ->
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
                    PokemonItem(pokemon = pokemon, onItemClicked = { onItemClicked(pokemon) })
                }
                if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                    LaunchedEffect(pokemon) {
                        onDelete(pokemon)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    PokemonTheme {
        FavoritesScreen(
            listOf(
                FavoritePokemon(1, "bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                FavoritePokemon(2, "ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
                FavoritePokemon(3, "venusaur", "https://pokeapi.co/api/v2/pokemon/3/"),
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
                .clickable { onItemClicked }
        ) {
            Text(
                text = pokemon.id.toString(),
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
            pokemon = FavoritePokemon(1, "bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            onItemClicked = {}
        )
    }
}