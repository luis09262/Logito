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
 * ☀️ SolarCoronaCanvas: Corona Solar y Lazos de Campo Magnético.
 * Espículas radiales de alta energía, lazos coronales parabólicos que emergen y se reconectan,
 * y micro-ascuas que derivan con flotación orgánica. Cero discos sólidos.
 */
@Composable
fun SolarCoronaCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF6D00),
    secondaryColor: Color = Color(0xFFFFD600),
    glowColor: Color = Color(0xFFFFAB00),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SolarCoronaEngine")

    // Ciclo de actividad coronal
    val flarePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(7500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SolarFlarePhase"
    )

    // Pulsación térmica del núcleo
    val thermalPulse by infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ThermalPulse"
    )

    val embers = remember {
        val rand = Random(505)
        List(18) {
            SolarEmber(
                baseAngle = rand.nextFloat() * 360f,
                speed = 0.4f + rand.nextFloat() * 0.6f,
                radiusScale = 0.95f + rand.nextFloat() * 0.35f,
                size = 1.0f + rand.nextFloat() * 1.8f,
                phase = rand.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Atmósfera térmica solar suave y degradada
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.72f to primaryColor.copy(alpha = 0.06f * intensity),
                    0.92f to glowColor.copy(alpha = 0.14f * intensity * thermalPulse),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.34f
            ),
            center = center,
            radius = baseRadius * 1.34f
        )

        // 2. Lazos magnéticos coronales (arcos Bezier parabólicos de 1.2dp)
        for (loop in 0 until 5) {
            val loopAngle = (flarePhase * 0.3f + loop * (2 * PI / 5f)).toFloat()
            val spanAngle = 0.22f // apertura angular del lazo
            val p1Angle = loopAngle - spanAngle / 2f
            val p2Angle = loopAngle + spanAngle / 2f

            val rBase = baseRadius * 1.04f
            val p1 = Offset(center.x + rBase * cos(p1Angle), center.y + rBase * sin(p1Angle))
            val p2 = Offset(center.x + rBase * cos(p2Angle), center.y + rBase * sin(p2Angle))

            val rApex = baseRadius * (1.20f + 0.08f * sin(flarePhase * 2f + loop))
            val apex = Offset(center.x + rApex * cos(loopAngle), center.y + rApex * sin(loopAngle))

            val path = Path().apply {
                moveTo(p1.x, p1.y)
                quadraticBezierTo(apex.x, apex.y, p2.x, p2.y)
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    listOf(
                        secondaryColor.copy(alpha = 0.3f * intensity),
                        glowColor.copy(alpha = 0.8f * intensity),
                        primaryColor.copy(alpha = 0.3f * intensity)
                    ),
                    start = p1,
                    end = p2
                ),
                style = Stroke(width = 1.2.dp.toPx())
            )
        }

        // 3. Espículas solares capilares (rayos finos de 0.8dp)
        val spiculeCount = 36
        for (s in 0 until spiculeCount) {
            val sAngle = (s.toFloat() / spiculeCount) * 2 * PI.toFloat()
            val noise = sin(s * 4.2f + flarePhase * 3f)
            val spiculeLength = (4.dp.toPx() + 6.dp.toPx() * (noise * 0.5f + 0.5f)) * thermalPulse
            val startR = baseRadius * 1.03f
            val endR = startR + spiculeLength

            val start = Offset(center.x + startR * cos(sAngle), center.y + startR * sin(sAngle))
            val end = Offset(center.x + endR * cos(sAngle), center.y + endR * sin(sAngle))

            drawLine(
                color = secondaryColor.copy(alpha = 0.35f * intensity),
                start = start,
                end = end,
                strokeWidth = 0.9.dp.toPx()
            )
        }

        // 4. Micro-ascuas solares flotantes
        embers.forEach { ember ->
            val angle = ember.baseAngle * PI / 180f + flarePhase * ember.speed * 0.3f
            val r = baseRadius * ember.radiusScale + sin(flarePhase * 2f + ember.phase) * 5.dp.toPx()
            val pos = Offset(center.x + r * cos(angle).toFloat(), center.y + r * sin(angle).toFloat())

            drawCircle(
                color = secondaryColor.copy(alpha = 0.8f * intensity),
                center = pos,
                radius = ember.size.dp.toPx()
            )
            drawCircle(
                color = Color.White,
                center = pos,
                radius = (ember.size * 0.5f).dp.toPx()
            )
        }
    }
}

private data class SolarEmber(
    val baseAngle: Float,
    val speed: Float,
    val radiusScale: Float,
    val size: Float,
    val phase: Float
)
