package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * 🚀 HyperspaceWarpCanvas: Salto Hiperespacial y Velocidad Luz.
 * Estelas de luz hiperdimensionales que se proyectan hacia el exterior en perspectiva 3D
 * radial, simulando un motor de curvatura warp acelerando a través del cosmos.
 */
@Composable
fun HyperspaceWarpCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E5FF),
    secondaryColor: Color = Color(0xFF7C4DFF),
    glowColor: Color = Color(0xFF80D8FF),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "HyperspaceEngine")

    val warpProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WarpProgress"
    )

    val starStreaks = remember {
        val rand = Random(303)
        List(36) { i ->
            WarpStreak(
                angle = (i * (360f / 36) + rand.nextFloat() * 6f),
                speed = 0.8f + rand.nextFloat() * 0.6f,
                streakLen = 12.dp + (rand.nextFloat() * 16.dp.value).dp,
                width = 1.0.dp + (rand.nextFloat() * 1.5.dp.value).dp,
                phase = rand.nextFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor túnel warp difuso
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity),
                    0.45f to primaryColor.copy(alpha = 0.06f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. Trazar cada estela de velocidad de la luz
        starStreaks.forEach { streak ->
            val localT = (warpProgress * streak.speed + streak.phase) % 1.0f
            // La estela nace cerca del marco y se acelera hacia afuera
            val startDist = frameRadius * (0.95f + localT * 0.45f)
            val len = streak.streakLen.toPx() * (0.5f + localT * 0.8f)
            val endDist = startDist + len

            val rad = streak.angle * PI / 180.0
            val pStart = Offset(
                center.x + (startDist * cos(rad)).toFloat(),
                center.y + (startDist * sin(rad)).toFloat()
            )
            val pEnd = Offset(
                center.x + (endDist * cos(rad)).toFloat(),
                center.y + (endDist * sin(rad)).toFloat()
            )

            val streakAlpha = (sin(localT * PI.toFloat())).coerceIn(0f, 1f) * intensity

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f * streakAlpha),
                        primaryColor.copy(alpha = 0.6f * streakAlpha),
                        Color.Transparent
                    ),
                    start = pStart,
                    end = pEnd
                ),
                start = pStart,
                end = pEnd,
                strokeWidth = streak.width.toPx(),
                cap = StrokeCap.Round,
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class WarpStreak(
    val angle: Float,
    val speed: Float,
    val streakLen: androidx.compose.ui.unit.Dp,
    val width: androidx.compose.ui.unit.Dp,
    val phase: Float
)
