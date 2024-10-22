package jp.android.pokemon.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.model.Sprites
import jp.android.pokemon.ui.theme.PokemonTheme
import jp.android.pokemon.viewmodel.PokemonViewModel


@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val pokemonDetails by viewModel.pokemonDetails.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.fetchPokemonDetails(pokemonId)
    }

    pokemonDetails?.let { details ->
        PokemonDetailScreen(
            pokemonDetails = details,
            navController = navController,
        )
    } ?: CircularProgressIndicator()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonDetailScreen(
    pokemonDetails: PokemonDetails,
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Pokemon Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.addToFavorite(
                            FavoritePokemon(
                                id = pokemonDetails.id,
                                name = pokemonDetails.name,
                                imageUrl = pokemonDetails.sprites.front_default
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Add to Favorites"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Column() {
                AsyncImage(
                    model = pokemonDetails.sprites.front_default,
                    contentDescription = "Pokemon Sprite",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Pokemon Name: ${pokemonDetails.name}")
                Text(text = "Height: ${pokemonDetails.height}")
                Text(text = "Weight: ${pokemonDetails.weight}")
                Text(text = "Sprite URL: ${pokemonDetails.sprites.front_default}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonDetailScreen() {
    val details = PokemonDetails(
        id = 25,
        name = "Pikachu",
        height = 4,
        weight = 60,
        sprites = Sprites(
            front_default = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"
        )
    )
    PokemonTheme {
        PokemonDetailScreen(
            pokemonDetails = details,
            navController = rememberNavController()
        )
    }
}