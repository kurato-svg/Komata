package app.kcs.komata.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.kcs.komata.core.model.ReaderPage
import app.kcs.komata.ui.components.KomataImage

@Composable
fun ReaderScreen(
    chapterTitle: String,
    pages: List<ReaderPage>,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text("Back", color = Color.White)
            }
            Text(
                text = chapterTitle,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${pages.size} pages",
                color = Color.LightGray,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        if (pages.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No pages", color = Color.White)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(pages, key = { it.index }) { page ->
                    KomataImage(
                        image = page.image,
                        contentDescription = "Page ${page.index + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(page.aspectRatio),
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
    }
}
