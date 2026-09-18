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

/**
 * 🔮 ArcanePlasmaCanvas: Listones de Energía Fluida y Plasma Arcano.
 * Ondas senoidales de fase armónica suave que fluyen alrededor del marco,
 * generando un vórtice etéreo con gradientes cromáticos continuos.
 * Cero esferas toscas: solo física de ondas suaves y micro-chispas de éter.
 */
@Composable
fun ArcanePlasmaCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFE040FB),
    secondaryColor: Color = Color(0xFF7C4DFF),
    glowColor: Color = Color(0xFFEA80FC),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ArcanePlasmaEngine")

    // Ciclo de fase armónica continua
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PlasmaPhase"
    )

    // Modulación de amplitud de respiración
    val breathingAmp by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingAmp"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Resplandor difuso perimétrico suave
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.74f to secondaryColor.copy(alpha = 0.05f * intensity),
                    0.94f to glowColor.copy(alpha = 0.12f * intensity * breathingAmp),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.32f
            ),
            center = center,
            radius = baseRadius * 1.32f
        )

        // 2. Tres listones de plasma sinusoidal entrelazados (armónicos 4, 6 y 8)
        val ribbons = listOf(
            Triple(4, 7.dp.toPx(), 1.4.dp.toPx()),
            Triple(6, 9.dp.toPx(), 1.0.dp.toPx()),
            Triple(8, 5.dp.toPx(), 0.8.dp.toPx())
        )

        ribbons.forEachIndexed { index, (harmonic, amp, strokeW) ->
            val path = Path()
            val pointsCount = 120
            val direction = if (index % 2 == 0) 1f else -1f
            val phaseOffset = wavePhase * direction + (index * PI / 3f).toFloat()

            for (i in 0..pointsCount) {
                val theta = (i.toFloat() / pointsCount) * 2 * PI.toFloat()
                // Modulación de onda con armónicos
                val deltaR = amp * sin(harmonic * theta + phaseOffset) * breathingAmp
                val r = baseRadius * (1.04f + index * 0.06f) + deltaR

                val x = center.x + r * cos(theta)
                val y = center.y + r * sin(theta)

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()

            val ribbonBrush = Brush.sweepGradient(
                listOf(
                    primaryColor.copy(alpha = 0.5f * intensity),
                    glowColor.copy(alpha = 0.85f * intensity),
                    secondaryColor.copy(alpha = 0.4f * intensity),
                    primaryColor.copy(alpha = 0.5f * intensity)
                ),
                center = center
            )

            drawPath(
                path = path,
                brush = ribbonBrush,
                style = Stroke(width = strokeW)
            )
        }

        // 3. Micro-filamentos radiales de plasma (pequeñas descargas capilares)
        for (f in 0 until 8) {
            val filamentAngle = (wavePhase * 0.5f + f * (PI / 4f)).toFloat()
            val innerR = baseRadius * 1.05f
            val outerR = baseRadius * (1.18f + 0.06f * sin(wavePhase * 3f + f))
            val start = Offset(center.x + innerR * cos(filamentAngle), center.y + innerR * sin(filamentAngle))
            val end = Offset(center.x + outerR * cos(filamentAngle), center.y + outerR * sin(filamentAngle))

            drawLine(
                brush = Brush.linearGradient(
                    listOf(
                        glowColor.copy(alpha = 0.6f * intensity),
                        Color.Transparent
                    ),
                    start = start,
                    end = end
                ),
                start = start,
                end = end,
                strokeWidth = 1.0.dp.toPx()
            )
        }
    }
}
