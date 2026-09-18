package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * 🕳️ GravitationalSingularityCanvas: Singularidad Gravitacional y Horizonte de Sucesos.
 * Disco de acreción relativista, anillos de fotones distorsionados por la gravedad,
 * filamentos de materia oscura que giran en espiral y destellos de radiación de Hawking.
 */
@Composable
fun GravitationalSingularityCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF7C4DFF),
    secondaryColor: Color = Color(0xFFE040FB),
    glowColor: Color = Color(0xFFEA80FC),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SingularityEngine")

    val accretionSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "AccretionSpin"
    )

    val eventHorizonPulse by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HorizonPulse"
    )

    val dustParticles = remember {
        val rand = Random(909)
        List(28) {
            SingularityDust(
                angle = rand.nextFloat() * 360f,
                speed = 0.8f + rand.nextFloat() * 1.5f,
                radialOffset = 0.96f + rand.nextFloat() * 0.35f,
                size = 1.0f + rand.nextFloat() * 1.8f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor difuso continuo
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity * eventHorizonPulse),
                    0.45f to primaryColor.copy(alpha = 0.06f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. 4 Arcos de acreción hiperbólicos
        for (i in 0 until 4) {
            val startAngle = (accretionSpin * 1.5f + i * 90f) * PI / 180.0
            val path = Path()
            val steps = 18
            for (s in 0..steps) {
                val progress = s / steps.toFloat()
                val spiralAngle = startAngle + progress * 1.2
                val r = frameRadius * (1.25f - progress * 0.25f)
                val x = center.x + (r * cos(spiralAngle)).toFloat()
                val y = center.y + (r * sin(spiralAngle)).toFloat()
                if (s == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent,
                        secondaryColor.copy(alpha = 0.7f * intensity),
                        Color.White.copy(alpha = 0.9f)
                    )
                ),
                style = Stroke(width = 2.0.dp.toPx(), cap = StrokeCap.Round),
                blendMode = BlendMode.Plus
            )
        }

        // 3. Polvo gravitacional en espiral
        dustParticles.forEach { dust ->
            val angle = (dust.angle + accretionSpin * dust.speed) * PI / 180.0
            val r = frameRadius * dust.radialOffset
            val pos = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.8f * intensity),
                center = pos,
                radius = dust.size.dp.toPx() * 0.7f,
                blendMode = BlendMode.Plus
            )
            drawCircle(
                color = secondaryColor.copy(alpha = 0.4f * intensity),
                center = pos,
                radius = dust.size.dp.toPx() * 1.4f,
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class SingularityDust(
    val angle: Float,
    val speed: Float,
    val radialOffset: Float,
    val size: Float
)
