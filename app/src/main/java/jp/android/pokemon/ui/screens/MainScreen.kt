package jp.android.pokemon.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jp.android.pokemon.ui.navigation.PokemonNavGraph
import jp.android.pokemon.ui.theme.PokemonTheme

@Composable
fun MainScreen (
    navController: NavHostController,
) {
    Scaffold (
        bottomBar = {
            NavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            PokemonNavGraph(navController = navController)
        }
    }
}

@Composable
fun NavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        NavItem("一覧", Icons.AutoMirrored.Filled.List, "pokemonList"),
        NavItem("お気に入り", Icons.Default.Favorite, "favorites"),
        NavItem("設定", Icons.Default.Settings, "settings")
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                // 現在のルートが親画面か、親画面に属するサブ画面であれば選択状態を維持
                selected = when (item.route) {
                    "pokemonList" -> currentRoute?.startsWith("pokemonList") == true || currentRoute?.startsWith("pokemonDetail") == true
                    "favorites" -> currentRoute?.startsWith("favorites") == true || currentRoute?.startsWith("favoriteDetail") == true
                    "settings" -> currentRoute?.startsWith("settings") == true || currentRoute?.startsWith("settingsDetail") == true
                    else -> false
                },
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,  // 選択されているときの色
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,  // 選択されていないときの色
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    PokemonTheme {
        NavigationBar(navController = rememberNavController())
    }
}