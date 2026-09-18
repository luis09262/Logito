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
 * 💎 PrismaticCrystalsCanvas: Cristales Prismáticos y Gemas Flotantes con Refractancia.
 * Cristales 3D facetados que orbitan el avatar, reflejando destellos de dispersión
 * cromática espectral (cian, magenta, dorado y blanco especular).
 */
@Composable
fun PrismaticCrystalsCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E5FF),
    secondaryColor: Color = Color(0xFFFF4081),
    glowColor: Color = Color(0xFFE040FB),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PrismaticEngine")

    // Órbita lenta de los cristales
    val orbitSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbitSpin"
    )

    // Rotación sobre su propio eje
    val crystalSelfSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SelfSpin"
    )

    val crystals = remember {
        val rand = Random(404)
        List(10) { i ->
            PrismGem(
                baseAngle = i * 36f,
                speed = 0.6f + rand.nextFloat() * 0.4f,
                radiusOffset = 1.06f + rand.nextFloat() * 0.12f,
                size = 5.dp + (rand.nextFloat() * 4.dp.value).dp,
                phase = rand.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor prismático difuso
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.10f * intensity),
                    0.45f to primaryColor.copy(alpha = 0.05f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. Dibujar cada Gema Facetada 3D
        crystals.forEach { gem ->
            val angle = gem.baseAngle * PI / 180.0 + (orbitSpin * gem.speed * 0.017)
            val wobble = sin(crystalSelfSpin * 0.05f + gem.phase) * 4.dp.toPx()
            val r = frameRadius * gem.radiusOffset + wobble
            val gemCenter = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            val selfRot = crystalSelfSpin * 1.5f + gem.phase * 57.29f
            val s = gem.size.toPx()

            rotate(degrees = selfRot, pivot = gemCenter) {
                // Faceta Izquierda
                val leftFacet = Path().apply {
                    moveTo(gemCenter.x, gemCenter.y - s)
                    lineTo(gemCenter.x - s * 0.6f, gemCenter.y)
                    lineTo(gemCenter.x, gemCenter.y + s)
                    close()
                }
                drawPath(
                    path = leftFacet,
                    color = primaryColor.copy(alpha = 0.75f * intensity)
                )

                // Faceta Derecha
                val rightFacet = Path().apply {
                    moveTo(gemCenter.x, gemCenter.y - s)
                    lineTo(gemCenter.x + s * 0.6f, gemCenter.y)
                    lineTo(gemCenter.x, gemCenter.y + s)
                    close()
                }
                drawPath(
                    path = rightFacet,
                    color = secondaryColor.copy(alpha = 0.75f * intensity)
                )

                // Línea de arista y destello especular blanco
                drawLine(
                    color = Color.White.copy(alpha = 0.9f),
                    start = Offset(gemCenter.x, gemCenter.y - s),
                    end = Offset(gemCenter.x, gemCenter.y + s),
                    strokeWidth = 1.0.dp.toPx(),
                    blendMode = BlendMode.Plus
                )

                // Destello en cruz en la cúspide
                val glintSize = 2.5.dp.toPx()
                drawLine(
                    color = Color.White,
                    start = Offset(gemCenter.x - glintSize, gemCenter.y - s),
                    end = Offset(gemCenter.x + glintSize, gemCenter.y - s),
                    strokeWidth = 0.8.dp.toPx(),
                    blendMode = BlendMode.Plus
                )
            }
        }
    }
}

private data class PrismGem(
    val baseAngle: Float,
    val speed: Float,
    val radiusOffset: Float,
    val size: androidx.compose.ui.unit.Dp,
    val phase: Float
)
