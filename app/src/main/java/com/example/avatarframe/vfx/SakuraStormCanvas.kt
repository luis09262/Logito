package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * 🌸 SakuraStormCanvas: Viento de Primavera y Pétalos de Cerezo Flotantes.
 * Tormenta de pétalos de sakura en deriva orbital tridimensional, con silueta
 * anatómica de pétalo (muesca apical), rotación angular y micro-polen brillante.
 */
@Composable
fun SakuraStormCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF4081),
    secondaryColor: Color = Color(0xFFFF80AB),
    glowColor: Color = Color(0xFFF8BBD0),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SakuraStormEngine")

    val breezeTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "BreezeTime"
    )

    val petalSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PetalSpin"
    )

    val petals = remember {
        val rand = Random(808)
        List(22) {
            SakuraPetal(
                baseAngle = rand.nextFloat() * 360f,
                speed = 0.4f + rand.nextFloat() * 0.5f,
                distance = 0.94f + rand.nextFloat() * 0.36f,
                width = 7.dp + (rand.nextFloat() * 5.dp.value).dp,
                height = 11.dp + (rand.nextFloat() * 6.dp.value).dp,
                spinSpeed = 1.0f + rand.nextFloat() * 2.0f,
                phase = rand.nextFloat() * 2 * PI.toFloat()
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

        // 2. Dibujar pétalos flotantes con silueta anatómica de sakura
        petals.forEach { petal ->
            val angle = petal.baseAngle * PI / 180.0 + (breezeTime * petal.speed)
            val r = frameRadius * petal.distance + sin(breezeTime * 2f + petal.phase) * 6.dp.toPx()
            val petalCenter = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            val selfRotation = petalSpin * petal.spinSpeed + petal.phase * 57.29f
            val pW = petal.width.toPx()
            val pH = petal.height.toPx() * (0.5f + 0.5f * abs(sin(breezeTime * petal.spinSpeed + petal.phase)))

            rotate(degrees = selfRotation, pivot = petalCenter) {
                val path = Path().apply {
                    moveTo(petalCenter.x, petalCenter.y - pH / 2f)
                    quadraticBezierTo(
                        petalCenter.x + pW / 2f, petalCenter.y - pH / 4f,
                        petalCenter.x + pW / 3f, petalCenter.y + pH / 2f
                    )
                    quadraticBezierTo(
                        petalCenter.x, petalCenter.y + pH * 0.45f,
                        petalCenter.x - pW / 3f, petalCenter.y + pH / 2f
                    )
                    quadraticBezierTo(
                        petalCenter.x - pW / 2f, petalCenter.y - pH / 4f,
                        petalCenter.x, petalCenter.y - pH / 2f
                    )
                    close()
                }

                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.85f * intensity),
                            secondaryColor.copy(alpha = 0.80f * intensity),
                            glowColor.copy(alpha = 0.95f)
                        ),
                        start = Offset(petalCenter.x, petalCenter.y + pH / 2f),
                        end = Offset(petalCenter.x, petalCenter.y - pH / 2f)
                    )
                )

                drawLine(
                    color = Color.White.copy(alpha = 0.45f),
                    start = Offset(petalCenter.x, petalCenter.y + pH * 0.35f),
                    end = Offset(petalCenter.x, petalCenter.y - pH * 0.25f),
                    strokeWidth = 0.7.dp.toPx()
                )
            }

            val pollenOffset = Offset(
                petalCenter.x + sin(breezeTime * 4f + petal.phase) * 4.dp.toPx(),
                petalCenter.y + cos(breezeTime * 4f + petal.phase) * 4.dp.toPx()
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.85f * intensity),
                center = pollenOffset,
                radius = 1.0.dp.toPx(),
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class SakuraPetal(
    val baseAngle: Float,
    val speed: Float,
    val distance: Float,
    val width: androidx.compose.ui.unit.Dp,
    val height: androidx.compose.ui.unit.Dp,
    val spinSpeed: Float,
    val phase: Float
)
