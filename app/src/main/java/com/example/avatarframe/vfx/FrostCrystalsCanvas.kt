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
 * ❄️ FrostCrystalsCanvas: Glaciar Cúbico y Red Creciente de Hielo Hexagonal.
 * Simetría molecular de 6 puntas, micro-cristales de hielo en suspensión,
 * líneas de fractura de cristal ultrafinas (0.8dp) y centelleo de polvo de diamante criogénico.
 */
@Composable
fun FrostCrystalsCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF80D8FF),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFFE1F5FE),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "FrostCrystalsEngine")

    // Rotación lenta de la red cristalina
    val crystalRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CrystalRotation"
    )

    // Shimmer de refracción del hielo
    val frostShimmer by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FrostShimmer"
    )

    val iceSparks = remember {
        val rand = Random(606)
        List(20) {
            IceSpark(
                baseAngle = rand.nextFloat() * 360f,
                distance = 0.90f + rand.nextFloat() * 0.36f,
                size = 1.2f + rand.nextFloat() * 1.6f,
                phase = rand.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Neblina criogénica sub-cero difusa
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.74f to primaryColor.copy(alpha = 0.05f * intensity),
                    0.92f to glowColor.copy(alpha = 0.13f * intensity * frostShimmer),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.32f
            ),
            center = center,
            radius = baseRadius * 1.32f
        )

        // 2. Anillo hexagonal de cristalización (simetría de 6 vértices)
        rotate(degrees = crystalRotation * 0.3f, pivot = center) {
            val rHex = baseRadius * 1.10f
            val hexPath = Path()
            for (i in 0 until 6) {
                val theta = (i * 60f) * PI / 180f
                val pt = Offset(
                    center.x + rHex * cos(theta).toFloat(),
                    center.y + rHex * sin(theta).toFloat()
                )
                if (i == 0) hexPath.moveTo(pt.x, pt.y) else hexPath.lineTo(pt.x, pt.y)
            }
            hexPath.close()

            drawPath(
                path = hexPath,
                color = primaryColor.copy(alpha = 0.35f * intensity),
                style = Stroke(
                    width = 1.0.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f))
                )
            )

            // 6 Espículas de cristal en cada vértice hexagonal
            for (i in 0 until 6) {
                val theta = (i * 60f) * PI / 180f
                val vStart = Offset(
                    center.x + rHex * cos(theta).toFloat(),
                    center.y + rHex * sin(theta).toFloat()
                )
                val vEnd = Offset(
                    center.x + (rHex + 8.dp.toPx()) * cos(theta).toFloat(),
                    center.y + (rHex + 8.dp.toPx()) * sin(theta).toFloat()
                )
                drawLine(
                    color = glowColor.copy(alpha = 0.75f * intensity * frostShimmer),
                    start = vStart,
                    end = vEnd,
                    strokeWidth = 1.2.dp.toPx()
                )

                // Ramitas secundarias de la espícula (micro-copo)
                val branchLen = 3.5.dp.toPx()
                val branchThetaL = theta + 0.5f
                val branchThetaR = theta - 0.5f
                val branchOrigin = Offset(
                    center.x + (rHex + 4.dp.toPx()) * cos(theta).toFloat(),
                    center.y + (rHex + 4.dp.toPx()) * sin(theta).toFloat()
                )
                drawLine(
                    color = secondaryColor.copy(alpha = 0.6f * intensity),
                    start = branchOrigin,
                    end = Offset(branchOrigin.x + branchLen * cos(branchThetaL).toFloat(), branchOrigin.y + branchLen * sin(branchThetaL).toFloat()),
                    strokeWidth = 0.8.dp.toPx()
                )
                drawLine(
                    color = secondaryColor.copy(alpha = 0.6f * intensity),
                    start = branchOrigin,
                    end = Offset(branchOrigin.x + branchLen * cos(branchThetaR).toFloat(), branchOrigin.y + branchLen * sin(branchThetaR).toFloat()),
                    strokeWidth = 0.8.dp.toPx()
                )
            }
        }

        // 3. Polvo de diamante criogénico en flotación
        iceSparks.forEach { spark ->
            val angle = spark.baseAngle * PI / 180f + crystalRotation * 0.005f
            val r = baseRadius * spark.distance + sin(crystalRotation * 0.05f + spark.phase) * 4.dp.toPx()
            val pos = Offset(center.x + r * cos(angle).toFloat(), center.y + r * sin(angle).toFloat())

            drawCircle(
                color = Color.White.copy(alpha = 0.9f * intensity),
                center = pos,
                radius = (spark.size * 0.7f).dp.toPx()
            )
            drawCircle(
                color = glowColor.copy(alpha = 0.35f * intensity),
                center = pos,
                radius = (spark.size * 1.5f).dp.toPx()
            )
        }
    }
}

private data class IceSpark(
    val baseAngle: Float,
    val distance: Float,
    val size: Float,
    val phase: Float
)
