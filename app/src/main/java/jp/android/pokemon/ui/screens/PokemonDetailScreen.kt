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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import jp.android.pokemon.data.model.FavoritePokemon
import jp.android.pokemon.domain.model.PokemonDetails
import jp.android.pokemon.domain.model.Sprites
import jp.android.pokemon.ui.components.ErrorView
import jp.android.pokemon.ui.components.LoadingView
import jp.android.pokemon.ui.state.PokemonDetailUiState
import jp.android.pokemon.ui.theme.PokemonTheme
import jp.android.pokemon.viewmodel.FavoriteViewModel
import jp.android.pokemon.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    navController: NavController,
    detailViewModel: PokemonDetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
) {
    val uiState by detailViewModel.uiState.collectAsState()
    val isFavorite by favoriteViewModel.isFavorite.collectAsState()

    LaunchedEffect(key1 = pokemonId) {
        detailViewModel.fetchPokemonDetails(pokemonId)
        favoriteViewModel.checkIfFavorite(pokemonId.toInt())
    }

    when (uiState) {
        is PokemonDetailUiState.Loading -> {
            LoadingView()
        }
        is PokemonDetailUiState.Success -> {
            val details = (uiState as PokemonDetailUiState.Success).details
            PokemonDetailScreen(
                pokemonDetails = details,
                isFavorite = isFavorite,
                popBackStack = {
                    navController.popBackStack()
                },
                deleteFavorite = {
                    favoriteViewModel.removeFavoriteByPokemonId(it)
                },
                addFavorite = {
                    favoriteViewModel.addToFavorite(it)
                }
            )
        }
        is PokemonDetailUiState.Error -> {
            val errorMessage = (uiState as PokemonDetailUiState.Error).message
            ErrorView(errorMessage, onRetry = {
                detailViewModel.fetchPokemonDetails(pokemonId)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonDetailScreen(
    pokemonDetails: PokemonDetails,
    isFavorite: Boolean,
    popBackStack: () -> Unit,
    addFavorite: (FavoritePokemon) -> Unit,
    deleteFavorite: (Int) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("ポケモン詳細") },
                navigationIcon = {
                    IconButton(onClick = { popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isFavorite) {
                            deleteFavorite(pokemonDetails.id)
                        } else {
                            addFavorite(
                                FavoritePokemon(
                                    pokemonId = pokemonDetails.id,
                                    name = pokemonDetails.name,
                                    imageUrl = pokemonDetails.sprites.front_default
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Add to Favorites",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
        ) {
            Column {
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
private fun PreviewPokemonContentViewView() {
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
            details,
            isFavorite = false,
            popBackStack = {},
            addFavorite = {},
            deleteFavorite = {}
        )
    }
}


@Composable
private fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorMessage)
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { onRetry() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Retry",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewErrorView() {
    PokemonTheme {
        ErrorView("An error occurred", onRetry = {})
    }
}