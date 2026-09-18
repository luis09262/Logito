package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * 🧬 BioluminescentAbyssCanvas: Abismo Oceánico Bioluminiscente.
 * Inspirado en profundidades abisales alienígenas: micro-medusas de luz que pulsan
 * hidrodinámicamente, tentáculos de esporas fotosintéticas y burbujas de oxígeno neón.
 */
@Composable
fun BioluminescentAbyssCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E676),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFFB9F6CA),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AbyssEngine")

    val jellyPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "JellyPulse"
    )

    val currentDrift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CurrentDrift"
    )

    val spores = remember {
        val rand = Random(202)
        List(24) {
            AbyssSpore(
                baseAngle = rand.nextFloat() * 360f,
                speed = 0.5f + rand.nextFloat() * 0.7f,
                radiusDist = 0.96f + rand.nextFloat() * 0.35f,
                size = 1.2f + rand.nextFloat() * 2.2f,
                phase = rand.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor abisal profundo difuso
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity),
                    0.5f to primaryColor.copy(alpha = 0.06f * intensity),
                    0.85f to secondaryColor.copy(alpha = 0.02f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. 6 Micro-Medusas de Luz que nadan alrededor del marco
        val jellyCount = 6
        for (j in 0 until jellyCount) {
            val angle = (j * (360f / jellyCount) * PI / 180.0) + (currentDrift * 0.4)
            val r = frameRadius * (1.10f + 0.08f * sin(currentDrift * 2f + j))
            val jCenter = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            val jSize = 6.dp.toPx() * jellyPulse

            // Cúpula / Campana de la medusa
            val bellPath = Path().apply {
                moveTo(jCenter.x - jSize, jCenter.y)
                quadraticBezierTo(jCenter.x, jCenter.y - jSize * 1.2f, jCenter.x + jSize, jCenter.y)
                quadraticBezierTo(jCenter.x, jCenter.y - jSize * 0.2f, jCenter.x - jSize, jCenter.y)
                close()
            }

            drawPath(
                path = bellPath,
                color = secondaryColor.copy(alpha = 0.75f * intensity),
                blendMode = BlendMode.Plus
            )

            // Tentáculos fluorescentes
            for (t in -2..2) {
                val tStartX = jCenter.x + (t * jSize * 0.35f)
                val tStartY = jCenter.y
                val tEndX = tStartX + sin(currentDrift * 3f + t) * 4.dp.toPx()
                val tEndY = tStartY + (8.dp.toPx() * jellyPulse)

                drawLine(
                    brush = Brush.verticalGradient(
                        listOf(glowColor.copy(alpha = 0.8f), Color.Transparent),
                        startY = tStartY,
                        endY = tEndY
                    ),
                    start = Offset(tStartX, tStartY),
                    end = Offset(tEndX, tEndY),
                    strokeWidth = 0.8.dp.toPx(),
                    blendMode = BlendMode.Plus
                )
            }
        }

        // 3. Esporas marinas flotantes
        spores.forEach { spore ->
            val angle = spore.baseAngle * PI / 180.0 + (currentDrift * spore.speed * 0.02)
            val r = frameRadius * spore.radiusDist + sin(currentDrift * 2f + spore.phase) * 5.dp.toPx()
            val pos = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            val sporeAlpha = (0.4f + 0.6f * abs(sin(currentDrift * 2f + spore.phase))) * intensity
            drawCircle(
                color = glowColor.copy(alpha = sporeAlpha),
                center = pos,
                radius = spore.size.dp.toPx() * 0.7f,
                blendMode = BlendMode.Plus
            )
            drawCircle(
                color = primaryColor.copy(alpha = 0.4f * sporeAlpha),
                center = pos,
                radius = spore.size.dp.toPx() * 1.6f,
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class AbyssSpore(
    val baseAngle: Float,
    val speed: Float,
    val radiusDist: Float,
    val size: Float,
    val phase: Float
)
