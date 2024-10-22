package jp.android.pokemon.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import jp.android.pokemon.ui.theme.PokemonTheme

data class TabItem(val title: String, val icon: ImageVector)

@Composable
fun PokemonTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem("一覧", Icons.AutoMirrored.Filled.List),
        TabItem("お気に入り", Icons.Default.Favorite),
        TabItem("設定", Icons.Default.Settings)
    )

    TabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(tabItem.title) },
                icon = { tabItem.icon }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonTabsPreview() {
    PokemonTheme {
        PokemonTabs(selectedTabIndex = 0, onTabSelected = {})
    }
}

