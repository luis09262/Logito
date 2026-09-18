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

/**
 * 🐉 DragonSoulCanvas: Fuego Espectral y Alma del Dragón.
 * Cintas de llama espectral esmeralda y cian que serpentean con dinámica ondulante
 * alrededor del marco, con escamas energéticas romboidales y llamaradas espirituales.
 */
@Composable
fun DragonSoulCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E676),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFFB9F6CA),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DragonSoulEngine")

    val dragonOrbit by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DragonOrbit"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (4 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WavePhase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor difuso continuo
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

        // 2. Dos Dragones de Fuego Espectral
        for (d in 0 until 2) {
            val headAngle = (dragonOrbit + d * 180f) * PI / 180.0
            val bodyPoints = 32
            val path = Path()

            for (p in 0 until bodyPoints) {
                val segAngle = headAngle - (p * 0.065)
                val bodyWave = sin(wavePhase + p * 0.45) * 6.5.dp.toPx()
                val r = frameRadius + bodyWave + (p * 0.28.dp.toPx())

                val x = center.x + (r * cos(segAngle)).toFloat()
                val y = center.y + (r * sin(segAngle)).toFloat()

                if (p == 0) path.moveTo(x, y) else path.lineTo(x, y)

                if (p % 4 == 0 && p < 24) {
                    val scaleSize = (4.2 - p * 0.12).dp.toPx()
                    val scalePath = Path().apply {
                        moveTo(x, y - scaleSize)
                        lineTo(x + scaleSize * 0.7f, y)
                        lineTo(x, y + scaleSize)
                        lineTo(x - scaleSize * 0.7f, y)
                        close()
                    }
                    drawPath(
                        path = scalePath,
                        color = (if (d == 0) primaryColor else secondaryColor).copy(alpha = (0.7f - p * 0.02f) * intensity),
                        blendMode = BlendMode.Plus
                    )
                }
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    listOf(
                        glowColor.copy(alpha = 0.85f * intensity),
                        if (d == 0) primaryColor else secondaryColor,
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 3.2.dp.toPx(), cap = StrokeCap.Round),
                blendMode = BlendMode.Plus
            )

            val headX = center.x + (frameRadius * cos(headAngle)).toFloat()
            val headY = center.y + (frameRadius * sin(headAngle)).toFloat()
            drawCircle(
                color = Color.White,
                radius = 2.0.dp.toPx(),
                center = Offset(headX, headY),
                blendMode = BlendMode.Plus
            )
            drawCircle(
                color = glowColor.copy(alpha = 0.8f),
                radius = 4.8.dp.toPx(),
                center = Offset(headX, headY),
                blendMode = BlendMode.Plus
            )
        }
    }
}
