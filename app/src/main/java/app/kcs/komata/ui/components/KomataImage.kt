package app.kcs.komata.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import app.kcs.komata.core.model.ImageRef
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun KomataImage(
    image: ImageRef,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val model = when (image) {
        is ImageRef.Resource -> image.resId
        is ImageRef.Network -> {
            val builder = ImageRequest.Builder(context)
                .data(image.url)
                .crossfade(true)

            if (image.headers.isNotEmpty()) {
                val headers = NetworkHeaders.Builder().apply {
                    image.headers.forEach { (key, value) -> set(key, value) }
                }.build()
                builder.httpHeaders(headers)
            }

            builder.build()
        }
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}
