package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * ✨ CelestialStardustCanvas: Polvo Estelar y Constelaciones Astrales.
 * Micro-partículas de polvo de estrellas, destellos en cruz de 4 puntas de precisión óptica
 * y finas líneas de constelación interconectadas con trazo capilar (0.8dp).
 */
@Composable
fun CelestialStardustCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFFD700),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFFFFF9C4),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CelestialStardustEngine")

    // Ciclo de deriva astral lenta
    val driftTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DriftTime"
    )

    // Centelleo de las micro-estrellas
    val twinklePulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TwinklePulse"
    )

    // Semillas fijas de partículas para evitar recomposiciones bruscas
    val starNodes = remember {
        val rand = Random(404)
        List(24) { i ->
            StarNode(
                baseAngle = rand.nextFloat() * 360f,
                distanceFraction = 0.88f + rand.nextFloat() * 0.38f,
                size = 1.2f + rand.nextFloat() * 1.8f,
                speed = 0.3f + rand.nextFloat() * 0.5f,
                twinklePhase = rand.nextFloat() * 2 * PI.toFloat(),
                isCrossStar = i % 4 == 0 // 1 de cada 4 es una micro-estrella en cruz
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Nebulosa de fondo suave y etérea
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.72f to primaryColor.copy(alpha = 0.05f * intensity),
                    0.92f to secondaryColor.copy(alpha = 0.10f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.30f
            ),
            center = center,
            radius = baseRadius * 1.30f
        )

        // Calcular posiciones dinámicas de las estrellas
        val currentPositions = starNodes.map { node ->
            val angle = (node.baseAngle * PI / 180f + driftTime * node.speed).toFloat()
            val r = baseRadius * node.distanceFraction + sin(driftTime * 2f + node.twinklePhase) * 6.dp.toPx()
            val pos = Offset(center.x + r * cos(angle), center.y + r * sin(angle))
            val alpha = (0.35f + 0.65f * abs(sin(driftTime * 3f + node.twinklePhase))) * intensity
            Triple(pos, alpha, node)
        }

        // 2. Líneas capilares de constelación entre estrellas cercanas (trazo ultra-fino de 0.8dp)
        val maxConnectDistance = 55.dp.toPx()
        for (i in 0 until currentPositions.size) {
            val (posA, alphaA, _) = currentPositions[i]
            for (j in (i + 1) until currentPositions.size) {
                val (posB, alphaB, _) = currentPositions[j]
                val dx = posA.x - posB.x
                val dy = posA.y - posB.y
                val dist = sqrt(dx * dx + dy * dy)
                if (dist < maxConnectDistance) {
                    val lineAlpha = (1f - dist / maxConnectDistance) * 0.28f * min(alphaA, alphaB)
                    drawLine(
                        color = glowColor.copy(alpha = lineAlpha),
                        start = posA,
                        end = posB,
                        strokeWidth = 0.8.dp.toPx()
                    )
                }
            }
        }

        // 3. Dibujar cada micro-estrella y estrellas en cruz
        currentPositions.forEach { (pos, alpha, node) ->
            if (node.isCrossStar) {
                // Micro-destello óptico en cruz de 4 puntas (delicado, 5-8dp total)
                val rayLength = 5.5.dp.toPx() * (0.8f + 0.4f * twinklePulse)
                val rayColor = Color.White.copy(alpha = 0.9f * alpha)

                // Rayo horizontal
                drawLine(
                    color = rayColor,
                    start = Offset(pos.x - rayLength, pos.y),
                    end = Offset(pos.x + rayLength, pos.y),
                    strokeWidth = 0.9.dp.toPx()
                )
                // Rayo vertical
                drawLine(
                    color = rayColor,
                    start = Offset(pos.x, pos.y - rayLength),
                    end = Offset(pos.x, pos.y + rayLength),
                    strokeWidth = 0.9.dp.toPx()
                )
                // Punto central estelar
                drawCircle(
                    color = Color.White,
                    center = pos,
                    radius = 1.2.dp.toPx()
                )
            } else {
                // Partícula de polvo estelar (micro punto fino de 1 a 2 dp)
                drawCircle(
                    color = glowColor.copy(alpha = 0.35f * alpha),
                    center = pos,
                    radius = (node.size * 1.5f).dp.toPx()
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f * alpha),
                    center = pos,
                    radius = (node.size * 0.6f).dp.toPx()
                )
            }
        }
    }
}

private data class StarNode(
    val baseAngle: Float,
    val distanceFraction: Float,
    val size: Float,
    val speed: Float,
    val twinklePhase: Float,
    val isCrossStar: Boolean
)
