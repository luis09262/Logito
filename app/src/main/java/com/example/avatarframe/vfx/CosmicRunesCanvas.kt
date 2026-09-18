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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * 🌌 CosmicRunesCanvas: Círculo Astral y Runas Cósmicas Ancestrales.
 * Inspirado en círculos de invocación de anime y sellos de ascensión de videojuegos AAA.
 * Dibuja 12 glifos rúnicos matemáticos generados con geometría procedural, líneas de
 * constelación orbitales y micro-estrellas con destellos en cruz.
 */
@Composable
fun CosmicRunesCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFD500F9),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFFEA80FC),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CosmicRunesEngine")

    // Rotación lenta de la rueda de runas
    val runeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RuneRotation"
    )

    // Contrarotación del anillo de constelaciones
    val starOrbit by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "StarOrbit"
    )

    // Pulso rúnico místico
    val runePulse by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RunePulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor nebuloso difuso
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity * runePulse),
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

        // 2. 12 Glifos Rúnicos Geométricos dibujados alrededor del marco
        val runeCount = 12
        val runeRingRadius = frameRadius * 1.08f

        for (i in 0 until runeCount) {
            val angleDeg = i * (360f / runeCount) + runeRotation
            val angleRad = angleDeg * PI / 180.0
            val runeCenter = Offset(
                center.x + (runeRingRadius * cos(angleRad)).toFloat(),
                center.y + (runeRingRadius * sin(angleRad)).toFloat()
            )

            val runeAlpha = (0.6f + 0.4f * sin(runePulse * 3f + i)) * intensity
            val runeSize = 4.5.dp.toPx()

            rotate(degrees = angleDeg + 90f, pivot = runeCenter) {
                // Dibujar glifo procedural único según el índice
                val runePath = Path().apply {
                    when (i % 4) {
                        0 -> { // Glifo tipo rombo místico
                            moveTo(runeCenter.x, runeCenter.y - runeSize)
                            lineTo(runeCenter.x + runeSize * 0.6f, runeCenter.y)
                            lineTo(runeCenter.x, runeCenter.y + runeSize)
                            lineTo(runeCenter.x - runeSize * 0.6f, runeCenter.y)
                            close()
                            moveTo(runeCenter.x - runeSize * 0.4f, runeCenter.y)
                            lineTo(runeCenter.x + runeSize * 0.4f, runeCenter.y)
                        }
                        1 -> { // Glifo de tridente astral
                            moveTo(runeCenter.x, runeCenter.y - runeSize)
                            lineTo(runeCenter.x, runeCenter.y + runeSize)
                            moveTo(runeCenter.x - runeSize * 0.5f, runeCenter.y - runeSize * 0.5f)
                            lineTo(runeCenter.x, runeCenter.y)
                            lineTo(runeCenter.x + runeSize * 0.5f, runeCenter.y - runeSize * 0.5f)
                        }
                        2 -> { // Glifo de orbe coronado
                            moveTo(runeCenter.x - runeSize * 0.5f, runeCenter.y + runeSize * 0.3f)
                            lineTo(runeCenter.x, runeCenter.y - runeSize * 0.7f)
                            lineTo(runeCenter.x + runeSize * 0.5f, runeCenter.y + runeSize * 0.3f)
                            moveTo(runeCenter.x - runeSize * 0.4f, runeCenter.y + runeSize * 0.5f)
                            lineTo(runeCenter.x + runeSize * 0.4f, runeCenter.y + runeSize * 0.5f)
                        }
                        else -> { // Glifo reloj de arena estelar
                            moveTo(runeCenter.x - runeSize * 0.5f, runeCenter.y - runeSize * 0.6f)
                            lineTo(runeCenter.x + runeSize * 0.5f, runeCenter.y - runeSize * 0.6f)
                            lineTo(runeCenter.x - runeSize * 0.5f, runeCenter.y + runeSize * 0.6f)
                            lineTo(runeCenter.x + runeSize * 0.5f, runeCenter.y + runeSize * 0.6f)
                            close()
                        }
                    }
                }

                drawPath(
                    path = runePath,
                    color = glowColor.copy(alpha = runeAlpha),
                    style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Plus
                )
                drawPath(
                    path = runePath,
                    color = Color.White.copy(alpha = runeAlpha * 0.8f),
                    style = Stroke(width = 0.6.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Plus
                )
            }
        }

        // 3. Estrellas de Constelación interconectadas
        val starCount = 8
        val starPoints = mutableListOf<Offset>()
        val constRadius = frameRadius * 1.16f

        for (s in 0 until starCount) {
            val angle = (s * (360f / starCount) + starOrbit) * PI / 180.0
            val wobble = sin(runePulse * 2f + s) * 5.dp.toPx()
            val pt = Offset(
                center.x + ((constRadius + wobble) * cos(angle)).toFloat(),
                center.y + ((constRadius + wobble) * sin(angle)).toFloat()
            )
            starPoints.add(pt)
        }

        // Líneas tenues de constelación
        for (j in 0 until starPoints.size) {
            val p1 = starPoints[j]
            val p2 = starPoints[(j + 1) % starPoints.size]
            drawLine(
                color = secondaryColor.copy(alpha = 0.25f * intensity),
                start = p1,
                end = p2,
                strokeWidth = 0.8.dp.toPx(),
                blendMode = BlendMode.Plus
            )

            // Destello en cruz en cada estrella
            val starCrossSize = 3.5.dp.toPx()
            drawLine(
                color = Color.White,
                start = Offset(p1.x - starCrossSize, p1.y),
                end = Offset(p1.x + starCrossSize, p1.y),
                strokeWidth = 0.8.dp.toPx(),
                blendMode = BlendMode.Plus
            )
            drawLine(
                color = Color.White,
                start = Offset(p1.x, p1.y - starCrossSize),
                end = Offset(p1.x, p1.y + starCrossSize),
                strokeWidth = 0.8.dp.toPx(),
                blendMode = BlendMode.Plus
            )
            drawCircle(
                color = glowColor.copy(alpha = 0.7f),
                radius = 2.5.dp.toPx(),
                center = p1,
                blendMode = BlendMode.Plus
            )
        }
    }
}
