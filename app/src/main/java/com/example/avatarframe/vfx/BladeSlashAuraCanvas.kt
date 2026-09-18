package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * ⚔️ BladeSlashAuraCanvas: Tajos y Cortes de Espada de Energía (Cyber Katana).
 * Inspirado en efectos visuales de combate anime / esports (Yasuo, Vergil, Genji).
 * Ráfagas de tajos de sable de luz a alta velocidad que cortan el espacio alrededor
 * del avatar, dejando estelas luminosas con gradiente de desvanecimiento e impactos de chispa.
 */
@Composable
fun BladeSlashAuraCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E5FF),
    secondaryColor: Color = Color(0xFFFF0055),
    glowColor: Color = Color(0xFF80D8FF),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BladeSlashEngine")

    // Ciclo de tajos secuenciales
    val slashCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SlashCycle"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor ambiental suave
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

        // 2. Renderizar 4 tajos angulares
        val slashes = listOf(
            SlashDef(startAngle = 45f, sweep = 110f, radiusMul = 1.08f, color = primaryColor),
            SlashDef(startAngle = 210f, sweep = 120f, radiusMul = 1.04f, color = secondaryColor),
            SlashDef(startAngle = 315f, sweep = 105f, radiusMul = 1.12f, color = primaryColor),
            SlashDef(startAngle = 135f, sweep = 115f, radiusMul = 1.06f, color = glowColor)
        )

        slashes.forEachIndexed { index, slash ->
            val localTime = (slashCycle - index).let { if (it < 0) it + 4f else it }
            // Progreso del tajo: 0..1 es el corte activo y desvanecimiento
            if (localTime in 0f..1.4f) {
                val progress = (localTime / 1.4f).coerceIn(0f, 1f)
                val slashAlpha = (1f - progress) * intensity

                val startRad = slash.startAngle * PI / 180.0
                val sweepRad = slash.sweep * (PI / 180.0) * (progress * 1.5f).coerceAtMost(1f)
                val r = frameRadius * slash.radiusMul

                val slashPath = Path()
                val steps = 16
                for (s in 0..steps) {
                    val angle = startRad + (sweepRad * s / steps)
                    val x = center.x + (r * cos(angle)).toFloat()
                    val y = center.y + (r * sin(angle)).toFloat()
                    if (s == 0) slashPath.moveTo(x, y) else slashPath.lineTo(x, y)
                }

                // Capa 1: Halo de estela ancha
                drawPath(
                    path = slashPath,
                    color = slash.color.copy(alpha = 0.5f * slashAlpha),
                    style = Stroke(width = (4.5f * (1f - progress)).dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Plus
                )

                // Capa 2: Núcleo afilado de corte blanco
                drawPath(
                    path = slashPath,
                    color = Color.White.copy(alpha = 0.95f * slashAlpha),
                    style = Stroke(width = 1.4.dp.toPx(), cap = StrokeCap.Round),
                    blendMode = BlendMode.Plus
                )

                // Punta cortante (Chispa de impacto)
                if (progress < 0.8f) {
                    val tipAngle = startRad + sweepRad
                    val tipX = center.x + (r * cos(tipAngle)).toFloat()
                    val tipY = center.y + (r * sin(tipAngle)).toFloat()

                    drawCircle(
                        color = Color.White,
                        radius = 2.8.dp.toPx(),
                        center = Offset(tipX, tipY),
                        blendMode = BlendMode.Plus
                    )
                    drawCircle(
                        color = slash.color.copy(alpha = 0.8f),
                        radius = 6.0.dp.toPx(),
                        center = Offset(tipX, tipY),
                        blendMode = BlendMode.Plus
                    )
                }
            }
        }
    }
}

private data class SlashDef(
    val startAngle: Float,
    val sweep: Float,
    val radiusMul: Float,
    val color: Color
)
