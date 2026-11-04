package cl.duoc.rinconsur.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimacionEntradaFormulario(
    visible: Boolean,
    contenido: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(500)
        ) + slideInVertically(
            animationSpec = tween(500),
            initialOffsetY = { altura -> altura / 2}
        ),
        content = contenido
    )
}

@Composable
fun Modifier.pulsoYRotacion(): Modifier{
    val transicion = rememberInfiniteTransition(
        label = "pulso_y_rotacion"
    )
    val escala by transicion.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "escala_logo"
    )
    val rotacion by transicion.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotacion_logo"
    )
    return this.graphicsLayer {
        scaleX = escala
        scaleY = escala
        rotationZ = rotacion
    }
}
