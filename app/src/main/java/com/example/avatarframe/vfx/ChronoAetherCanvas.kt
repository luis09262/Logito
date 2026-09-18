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

/**
 * ⏳ ChronoAetherCanvas: Éter Cronológico y Geometría Sagrada Temporal.
 * Anillos astrolábicos dobles contra-rotatorios con divisiones angulares de precisión,
 * espirales armónicas de proporción áurea y partículas de tiempo suspendido.
 */
@Composable
fun ChronoAetherCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFFAB00),
    secondaryColor: Color = Color(0xFFFFD54F),
    glowColor: Color = Color(0xFFFFF59D),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ChronoAetherEngine")

    // Rotación del anillo astrolábico principal
    val clockwiseTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ClockwiseTime"
    )

    // Contra-rotación del anillo interior
    val counterTime by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CounterTime"
    )

    // Pulso temporal
    val chronoPulse by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ChronoPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Resplandor áureo temporal difuso
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.75f to primaryColor.copy(alpha = 0.04f * intensity),
                    0.94f to glowColor.copy(alpha = 0.12f * intensity * chronoPulse),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.30f
            ),
            center = center,
            radius = baseRadius * 1.30f
        )

        // 2. Anillo astrolábico exterior (gira en sentido horario) con marcas horarias finas
        rotate(degrees = clockwiseTime, pivot = center) {
            val rOuter = baseRadius * 1.14f
            drawCircle(
                color = primaryColor.copy(alpha = 0.35f * intensity),
                center = center,
                radius = rOuter,
                style = Stroke(
                    width = 1.1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 8f, 6f, 8f))
                )
            )

            // 12 marcas astrolábicas (como horas celestiales)
            for (h in 0 until 12) {
                val hAngle = (h * 30f) * PI / 180f
                val pStart = Offset(
                    center.x + (rOuter - 4.dp.toPx()) * cos(hAngle).toFloat(),
                    center.y + (rOuter - 4.dp.toPx()) * sin(hAngle).toFloat()
                )
                val pEnd = Offset(
                    center.x + (rOuter + 4.dp.toPx()) * cos(hAngle).toFloat(),
                    center.y + (rOuter + 4.dp.toPx()) * sin(hAngle).toFloat()
                )
                drawLine(
                    color = glowColor.copy(alpha = 0.7f * intensity),
                    start = pStart,
                    end = pEnd,
                    strokeWidth = 1.2.dp.toPx()
                )
            }
        }

        // 3. Anillo geométrico interior (contra-rotación) con triángulos de geometría sagrada
        rotate(degrees = counterTime, pivot = center) {
            val rInner = baseRadius * 1.05f
            drawCircle(
                color = secondaryColor.copy(alpha = 0.4f * intensity),
                center = center,
                radius = rInner,
                style = Stroke(
                    width = 0.9.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
                )
            )

            // 6 micro-diamantes sagrados en el anillo interior
            for (d in 0 until 6) {
                val dAngle = (d * 60f) * PI / 180f
                val dPos = Offset(
                    center.x + rInner * cos(dAngle).toFloat(),
                    center.y + rInner * sin(dAngle).toFloat()
                )
                // Micro-rombo de 2.5dp
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f * intensity),
                    center = dPos,
                    radius = 1.6.dp.toPx()
                )
            }
        }

        // 4. Espirales de tiempo (4 brazos curvos tenues que convergen)
        for (arm in 0 until 4) {
            val armOffset = (arm * PI / 2f).toFloat() + clockwiseTime * 0.015f
            val path = Path()
            val steps = 30
            for (s in 0..steps) {
                val t = s.toFloat() / steps
                val spiralTheta = armOffset + t * 0.9f
                val spiralR = baseRadius * (1.02f + t * 0.20f)
                val pt = Offset(
                    center.x + spiralR * cos(spiralTheta),
                    center.y + spiralR * sin(spiralTheta)
                )
                if (s == 0) path.moveTo(pt.x, pt.y) else path.lineTo(pt.x, pt.y)
            }
            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    listOf(
                        secondaryColor.copy(alpha = 0.4f * intensity),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 0.8.dp.toPx())
            )
        }
    }
}
