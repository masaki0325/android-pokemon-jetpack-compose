package jp.android.pokemon.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.android.pokemon.util.AppInfoProvider
import jp.android.pokemon.ui.theme.PokemonTheme

@Composable
fun SettingsScreen (
) {
    val context = LocalContext.current
    val appInfoProvider = remember { AppInfoProvider(context) }

    SettingsScreen(
        appVersion = appInfoProvider.getAppVersion(),
        buildNumber = appInfoProvider.getBuildNumber()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen (
    appVersion: String,
    buildNumber: String,
) {
    var isDarkTheme by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("設定") },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            HorizontalDivider()
            Text(
                "アプリ情報",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            ListItem(
                headlineContent = { Text("バージョン") },
                trailingContent = { Text(appVersion, fontSize = 16.sp) },
            )
            ListItem(
                headlineContent = { Text("ビルド番号") },
                trailingContent = { Text(buildNumber, fontSize = 16.sp) },
            )
            HorizontalDivider()
            Text(
                "テーマ",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            ListItem(
                headlineContent = { Text(text = "テーマ") },
                supportingContent = {
                    Text(text = if (isDarkTheme) "ダークテーマ" else "ライトテーマ")
                },
                trailingContent = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SettingsScreenLightPreview() {
    PokemonTheme {
        SettingsScreen(
            appVersion = "1.0.0",
            buildNumber = "1"
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenNightPreview() {
    PokemonTheme {
        SettingsScreen(
            appVersion = "1.0.0",
            buildNumber = "1"
        )
    }
}