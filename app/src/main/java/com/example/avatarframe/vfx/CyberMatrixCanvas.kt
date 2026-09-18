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
 * 🌐 CyberMatrixCanvas: Matriz Holográfica de Telemetría Cyberpunk / Sci-Fi.
 * Retícula de mira vectorial, marcas angulares de precisión militar (tick marks de 3dp),
 * barrido de radar de baja persistencia y paquetes de datos en bus perimétrico.
 */
@Composable
fun CyberMatrixCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E676),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFF69F0AE),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CyberMatrixEngine")

    // Rotación del anillo exterior de telemetría
    val hudRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "HudRotation"
    )

    // Barrido de radar angular
    val scanAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RadarScan"
    )

    // Pulso de datos binarios
    val dataPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DataPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Barrido de haz cónico de radar tenue (ángulo de 40 grados)
        drawArc(
            brush = Brush.sweepGradient(
                listOf(
                    Color.Transparent,
                    secondaryColor.copy(alpha = 0.02f * intensity),
                    primaryColor.copy(alpha = 0.14f * intensity),
                    Color.Transparent
                ),
                center = center
            ),
            startAngle = scanAngle,
            sweepAngle = 45f,
            useCenter = true,
            topLeft = Offset(center.x - baseRadius * 1.25f, center.y - baseRadius * 1.25f),
            size = androidx.compose.ui.geometry.Size(baseRadius * 2.5f, baseRadius * 2.5f)
        )

        // 2. Anillo de precisión con marcas de grado (ticks de 3.5dp a intervalos de 15°)
        rotate(degrees = hudRotation * 0.4f, pivot = center) {
            val tickRadius = baseRadius * 1.06f
            for (deg in 0 until 360 step 15) {
                val rad = deg * PI / 180f
                val isMajor = deg % 45 == 0
                val tickLength = if (isMajor) 5.dp.toPx() else 2.8.dp.toPx()
                val tickWidth = if (isMajor) 1.2.dp.toPx() else 0.8.dp.toPx()
                val alpha = if (isMajor) 0.65f * intensity else 0.35f * intensity

                val startPos = Offset(
                    center.x + tickRadius * cos(rad).toFloat(),
                    center.y + tickRadius * sin(rad).toFloat()
                )
                val endPos = Offset(
                    center.x + (tickRadius + tickLength) * cos(rad).toFloat(),
                    center.y + (tickRadius + tickLength) * sin(rad).toFloat()
                )

                drawLine(
                    color = if (isMajor) primaryColor.copy(alpha = alpha) else secondaryColor.copy(alpha = alpha),
                    start = startPos,
                    end = endPos,
                    strokeWidth = tickWidth
                )
            }
        }

        // 3. Arcos de retícula de brackets hexagonales exteriores
        rotate(degrees = -hudRotation * 0.6f, pivot = center) {
            val bracketRadius = baseRadius * 1.16f
            for (quadrant in listOf(0f, 90f, 180f, 270f)) {
                drawArc(
                    color = glowColor.copy(alpha = 0.45f * intensity),
                    startAngle = quadrant + 10f,
                    sweepAngle = 25f,
                    useCenter = false,
                    topLeft = Offset(center.x - bracketRadius, center.y - bracketRadius),
                    size = androidx.compose.ui.geometry.Size(bracketRadius * 2, bracketRadius * 2),
                    style = Stroke(width = 1.4.dp.toPx())
                )

                // Micro-punto de marcador en la esquina del bracket
                val markerRad = (quadrant + 10f) * PI / 180f
                val markerPos = Offset(
                    center.x + bracketRadius * cos(markerRad).toFloat(),
                    center.y + bracketRadius * sin(markerRad).toFloat()
                )
                drawRect(
                    color = Color.White.copy(alpha = 0.9f * intensity),
                    topLeft = Offset(markerPos.x - 1.5.dp.toPx(), markerPos.y - 1.5.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(3.dp.toPx(), 3.dp.toPx())
                )
            }
        }

        // 4. Paquetes de datos binarios viajando sobre la guía perimétrica
        val packetRadius = baseRadius * 1.0f
        for (p in 0 until 4) {
            val pAngle = (dataPulse * 360f + p * 90f) * PI / 180f
            val packetPos = Offset(
                center.x + packetRadius * cos(pAngle).toFloat(),
                center.y + packetRadius * sin(pAngle).toFloat()
            )

            drawCircle(
                color = primaryColor.copy(alpha = 0.8f * intensity),
                center = packetPos,
                radius = 2.dp.toPx()
            )
            drawCircle(
                color = Color.White,
                center = packetPos,
                radius = 1.dp.toPx()
            )
        }
    }
}
