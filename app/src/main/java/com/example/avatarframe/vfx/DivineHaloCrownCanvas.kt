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
 * 👑 DivineHaloCrownCanvas: Corona Celestial y Mandala de Luz Sagrada.
 * Inspirado en halos angelicos de altares divinos: rayos de luz radiantes
 * con puntas afiladas, filamentos solares y polvo de oro místico.
 */
@Composable
fun DivineHaloCrownCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFFD700),
    secondaryColor: Color = Color(0xFFFFB300),
    glowColor: Color = Color(0xFFFFF9C4),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DivineHaloEngine")

    val haloRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "HaloRotation"
    )

    val holyBreath by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HolyBreath"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor dorado celestial difuso
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.14f * intensity * holyBreath),
                    0.45f to primaryColor.copy(alpha = 0.08f * intensity * holyBreath),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. 24 Rayos Solares de Corona Angélica
        val rayCount = 24
        for (i in 0 until rayCount) {
            val angle = (i * (360f / rayCount) + haloRotation) * PI / 180.0
            val isMajorRay = i % 2 == 0
            val rayLen = (if (isMajorRay) 18.dp.toPx() else 10.dp.toPx()) * holyBreath
            val startR = frameRadius * 0.98f
            val endR = frameRadius + rayLen

            val pStart = Offset(
                center.x + (startR * cos(angle)).toFloat(),
                center.y + (startR * sin(angle)).toFloat()
            )
            val pEnd = Offset(
                center.x + (endR * cos(angle)).toFloat(),
                center.y + (endR * sin(angle)).toFloat()
            )

            drawLine(
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.95f * intensity),
                        primaryColor.copy(alpha = 0.7f * intensity),
                        Color.Transparent
                    ),
                    start = pStart,
                    end = pEnd
                ),
                start = pStart,
                end = pEnd,
                strokeWidth = if (isMajorRay) 1.8.dp.toPx() else 1.0.dp.toPx(),
                cap = StrokeCap.Round,
                blendMode = BlendMode.Plus
            )

            // Destello en la punta de los rayos mayores
            if (isMajorRay) {
                drawCircle(
                    color = Color.White,
                    radius = 1.4.dp.toPx(),
                    center = pEnd,
                    blendMode = BlendMode.Plus
                )
            }
        }
    }
}
