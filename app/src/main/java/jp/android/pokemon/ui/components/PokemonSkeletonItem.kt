package jp.android.pokemon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.placeholder
import jp.android.pokemon.ui.theme.PokemonTheme

@Composable
fun PokemonSkeletonItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .placeholder(
                    visible = true,
                    shape = RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.fade(),
                )
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .weight(1f)
                .placeholder(
                    visible = true,
                    shape = RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.fade(),
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonSkeletonItemPreview() {
    PokemonTheme {
        PokemonSkeletonItem()
    }
}