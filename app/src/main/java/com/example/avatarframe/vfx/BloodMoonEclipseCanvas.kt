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
 * 🩸 BloodMoonEclipseCanvas: Eclipse de Luna de Sangre y Miasma Oscuro.
 * Corona de eclipse carmesí profundo con rayos estriados que emergen del marco,
 * niebla de miasma oscuro y micro-cenizas de obsidiana ascendentes.
 */
@Composable
fun BloodMoonEclipseCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF1744),
    secondaryColor: Color = Color(0xFFD50000),
    glowColor: Color = Color(0xFFFF5252),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BloodMoonEngine")

    val coronaPulse by infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.16f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CoronaPulse"
    )

    val miasmaSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "MiasmaSpin"
    )

    val ashes = remember {
        val rand = Random(505)
        List(22) {
            BloodAsh(
                angle = rand.nextFloat() * 360f,
                speed = 0.4f + rand.nextFloat() * 0.6f,
                initialRadius = 0.98f + rand.nextFloat() * 0.32f,
                size = 1.0f + rand.nextFloat() * 1.8f,
                phase = rand.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor carmesí de eclipse difuso y continuo
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity * coronaPulse),
                    0.45f to primaryColor.copy(alpha = 0.08f * intensity),
                    0.85f to secondaryColor.copy(alpha = 0.02f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. 16 Rayos de Corona de Eclipse Solar/Lunar estriados
        val rayCount = 16
        for (r in 0 until rayCount) {
            val angle = (r * (360f / rayCount) + miasmaSpin * 0.5f) * PI / 180.0
            val rayLength = (10.dp.toPx() + 8.dp.toPx() * sin(miasmaSpin * 0.05f + r * 2f)) * coronaPulse
            val startR = frameRadius * 0.96f
            val endR = frameRadius + rayLength

            val startPt = Offset(
                center.x + (startR * cos(angle)).toFloat(),
                center.y + (startR * sin(angle)).toFloat()
            )
            val endPt = Offset(
                center.x + (endR * cos(angle)).toFloat(),
                center.y + (endR * sin(angle)).toFloat()
            )

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.75f * intensity),
                        primaryColor.copy(alpha = 0.35f * intensity),
                        Color.Transparent
                    ),
                    start = startPt,
                    end = endPt
                ),
                start = startPt,
                end = endPt,
                strokeWidth = (2.2f * coronaPulse).dp.toPx(),
                cap = StrokeCap.Round,
                blendMode = BlendMode.Plus
            )
        }

        // 3. Cenizas flotantes de sangre
        ashes.forEach { ash ->
            val angle = ash.angle * PI / 180.0 + (miasmaSpin * ash.speed * 0.015)
            val r = frameRadius * ash.initialRadius + sin(miasmaSpin * 0.04f + ash.phase) * 6.dp.toPx()
            val pos = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            drawCircle(
                color = primaryColor.copy(alpha = 0.7f * intensity),
                center = pos,
                radius = ash.size.dp.toPx(),
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class BloodAsh(
    val angle: Float,
    val speed: Float,
    val initialRadius: Float,
    val size: Float,
    val phase: Float
)
