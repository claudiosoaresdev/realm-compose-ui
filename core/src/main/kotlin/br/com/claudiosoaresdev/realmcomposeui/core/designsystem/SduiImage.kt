package br.com.claudiosoaresdev.realmcomposeui.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import br.com.claudiosoaresdev.realmcomposeui.core.network.RealmNetwork

/**
 * O payload manda caminho relativo (`/assets/targaryen/members/daemon_targaryen.png`),
 * servido pelo próprio json-server (`npx json-server db.json --static ./public`).
 */
fun String.toAssetUrl(): String =
    if (startsWith("http://") || startsWith("https://")) {
        this
    } else {
        RealmNetwork.BASE_URL.trimEnd('/') + "/" + trimStart('/')
    }

/**
 * Imagem do payload. Caminho nulo, arquivo ausente ou erro de rede caem no mesmo
 * placeholder — a tela nunca fica quebrada por causa de asset.
 */
@Composable
fun SduiImage(
    path: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallbackLabel: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    /** `false` deixa o espaço transparente quando a imagem falha — usado no fundo de tela. */
    opaqueFallback: Boolean = true,
) {
    if (path == null) {
        ImageFallback(label = fallbackLabel, opaque = opaqueFallback, modifier = modifier)
        return
    }
    SubcomposeAsyncImage(
        model = path.toAssetUrl(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
            is AsyncImagePainter.State.Error -> ImageFallback(
                label = fallbackLabel,
                opaque = opaqueFallback,
                modifier = Modifier.fillMaxSize(),
            )

            else -> ImageFallback(
                label = null,
                opaque = opaqueFallback,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ImageFallback(label: String?, opaque: Boolean, modifier: Modifier = Modifier) {
    val theme = LocalSduiTheme.current
    Box(
        modifier = if (opaque) {
            modifier.background(theme.colors.surfaceVariant.toColor())
        } else {
            modifier
        },
        contentAlignment = Alignment.Center,
    ) {
        if (!label.isNullOrBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = theme.colors.textDisabled.toColor(),
            )
        }
    }
}
