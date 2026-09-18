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
 * ⚡ ArcLightningCanvas: Rayos Eléctricos y Tormenta de Plasma Perimétrica.
 * Adaptativo a cada marco sin círculos toscos ni discos de fondo.
 * Arcos voltaicos de plasma vivo que reptan y bordean la silueta con fractales
 * estocásticos, ramificaciones (forks) y núcleos de ionización blanco puro.
 */
@Composable
fun ArcLightningCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E5FF),
    secondaryColor: Color = Color(0xFF7C4DFF),
    glowColor: Color = Color(0xFF80D8FF),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ArcLightningEngine")

    // Ciclo de rotación de los polos de descarga
    val orbitTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "LightningOrbit"
    )

    // Jitter estocástico de alta frecuencia (simula descargas eléctricas a 30-60 fps)
    val fastJitter by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(110, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "FastJitter"
    )

    // Pulso de descarga intermitente
    val flashPulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(360, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlashPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor difuso continuo y suave (sin ningún borde de disco)
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity * flashPulse),
                    0.45f to primaryColor.copy(alpha = 0.06f * intensity * flashPulse),
                    0.85f to secondaryColor.copy(alpha = 0.02f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // Semilla pseudoaleatoria sincronizada con fastJitter
        val seed = (fastJitter * 9999).toInt()
        val rng = Random(seed)

        // 2. Dibujar 6 Arcos Eléctricos Principales que reptan por el contorno
        val boltCount = 6
        for (b in 0 until boltCount) {
            val baseAngleDeg = (b * (360f / boltCount) + orbitTime + (b * 15f)) % 360f
            val arcSpanDeg = 34f + rng.nextFloat() * 22f
            val segments = 8

            val boltPath = Path()
            val branchPaths = mutableListOf<Path>()

            var curAngle = baseAngleDeg
            val initialR = frameRadius * (0.98f + (rng.nextFloat() - 0.5f) * 0.04f)
            val rad0 = curAngle * PI / 180.0
            val p0 = Offset(
                center.x + (initialR * cos(rad0)).toFloat(),
                center.y + (initialR * sin(rad0)).toFloat()
            )
            boltPath.moveTo(p0.x, p0.y)

            var lastPoint = p0
            val angleStep = arcSpanDeg / segments

            for (s in 1..segments) {
                curAngle += angleStep
                val radialNoise = (rng.nextFloat() - 0.5f) * 14.dp.toPx()
                val currentR = frameRadius + radialNoise
                val currentRad = curAngle * PI / 180.0
                val nextPoint = Offset(
                    center.x + (currentR * cos(currentRad)).toFloat(),
                    center.y + (currentR * sin(currentRad)).toFloat()
                )

                boltPath.lineTo(nextPoint.x, nextPoint.y)

                // Ramificaciones secundarias (forks) hacia el exterior
                if (rng.nextFloat() < 0.35f && s in 2..6) {
                    val branchPath = Path()
                    branchPath.moveTo(nextPoint.x, nextPoint.y)
                    val branchSpan = 14.dp.toPx()
                    val branchAngle = currentRad + (if (rng.nextBoolean()) 0.25 else -0.25)
                    val branchR = currentR + branchSpan * (0.6f + rng.nextFloat() * 0.5f)
                    val branchTip = Offset(
                        center.x + (branchR * cos(branchAngle)).toFloat(),
                        center.y + (branchR * sin(branchAngle)).toFloat()
                    )
                    branchPath.lineTo(branchTip.x, branchTip.y)
                    branchPaths.add(branchPath)

                    drawCircle(
                        color = Color.White,
                        radius = 1.2.dp.toPx(),
                        center = branchTip,
                        blendMode = BlendMode.Plus
                    )
                }

                lastPoint = nextPoint
            }

            // Capa A: Halo exterior de plasma
            drawPath(
                path = boltPath,
                color = primaryColor.copy(alpha = 0.60f * intensity),
                style = Stroke(width = 3.6.dp.toPx(), cap = StrokeCap.Round),
                blendMode = BlendMode.Plus
            )

            // Capa B: Núcleo medio de descarga
            drawPath(
                path = boltPath,
                color = glowColor.copy(alpha = 0.85f * intensity),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round),
                blendMode = BlendMode.Plus
            )

            // Capa C: Filamento blanco de ionización pura
            drawPath(
                path = boltPath,
                color = Color.White.copy(alpha = 0.95f),
                style = Stroke(width = 0.85.dp.toPx(), cap = StrokeCap.Round),
                blendMode = BlendMode.Plus
            )

            // Ramificaciones
            branchPaths.forEach { branch ->
                drawPath(
                    path = branch,
                    color = primaryColor.copy(alpha = 0.5f * intensity),
                    style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Plus
                )
                drawPath(
                    path = branch,
                    color = Color.White.copy(alpha = 0.9f),
                    style = Stroke(width = 0.75.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Plus
                )
            }

            // Chispa brillante en la punta del rayo
            drawCircle(
                color = Color.White,
                radius = 2.0.dp.toPx(),
                center = lastPoint,
                blendMode = BlendMode.Plus
            )
            drawCircle(
                color = glowColor.copy(alpha = 0.7f),
                radius = 4.2.dp.toPx(),
                center = lastPoint,
                blendMode = BlendMode.Plus
            )
        }

        // 3. Arcos cruzados de alto voltaje
        for (i in 0 until 3) {
            val jumpAngle1 = (orbitTime * 2f + i * 120f) * PI / 180.0
            val jumpAngle2 = jumpAngle1 + 0.35
            val r = frameRadius * (1.01f + 0.04f * sin(fastJitter * 20f + i))

            val pA = Offset(
                center.x + (r * cos(jumpAngle1)).toFloat(),
                center.y + (r * sin(jumpAngle1)).toFloat()
            )
            val pB = Offset(
                center.x + (r * cos(jumpAngle2)).toFloat(),
                center.y + (r * sin(jumpAngle2)).toFloat()
            )

            drawLine(
                color = secondaryColor.copy(alpha = 0.7f * intensity),
                start = pA,
                end = pB,
                strokeWidth = 1.6.dp.toPx(),
                blendMode = BlendMode.Plus
            )
            drawLine(
                color = Color.White,
                start = pA,
                end = pB,
                strokeWidth = 0.75.dp.toPx(),
                blendMode = BlendMode.Plus
            )
        }
    }
}
