package jp.android.pokemon

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun PokemonNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "pokemonList") {
        composable("pokemonList") {
            PokemonListScreen(navController = navController)
        }
        composable("favorites") {
            FavoritesScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen()
        }
        composable(
            "pokemonDetail/{pokemonId}",
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")
            pokemonId?.let {
                PokemonDetailScreen(pokemonId = it, navController = navController)
            }
        }
    }

}