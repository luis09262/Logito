package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * ⚛️ QuantumOrbitalsCanvas: Órbitas Cuánticas y Singularidad Relativista.
 * Diseñado con precisión geométrica: elipses inclinadas con trazos discontinuos finos,
 * micro-nodos cuánticos en traslación armónica y anillos de densidad de probabilidad.
 * Sin esferas toscas: solo líneas matemáticas sutiles y micro-brillos translúcidos.
 */
@Composable
fun QuantumOrbitalsCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF7C4DFF),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFFEA80FC),
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "QuantumOrbitalsEngine")

    // Rotación continua de los planos orbitales
    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbitPlaneRotation"
    )

    // Desplazamiento orbital de los micro-nodos
    val particlePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "QuantumNodePhase"
    )

    // Pulsación suave de la fluctuación cuántica
    val fluxPulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "QuantumFluxPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * 0.44f

        // 1. Aura de flujo de fondo hiper-suave (delicado halo difuso sin bordes duros)
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.65f to primaryColor.copy(alpha = 0.04f * intensity),
                    0.88f to glowColor.copy(alpha = 0.12f * intensity * fluxPulse),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.35f
            ),
            center = center,
            radius = baseRadius * 1.35f
        )

        // 2. Anillo de singularidad concéntrico exterior con trazos ultra-finos
        drawCircle(
            color = primaryColor.copy(alpha = 0.35f * intensity),
            center = center,
            radius = baseRadius * 1.08f * fluxPulse,
            style = Stroke(
                width = 1.2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), orbitRotation * 0.6f)
            )
        )

        // 3. Tres planos orbitales elípticos inclinados (30°, 90°, 150°)
        val orbitalPlanes = listOf(
            Triple(30f, 1.14f, 0.42f),
            Triple(90f, 1.18f, 0.38f),
            Triple(150f, 1.12f, 0.45f)
        )

        orbitalPlanes.forEachIndexed { index, (tiltAngle, aMult, bMult) ->
            rotate(degrees = tiltAngle + orbitRotation * 0.25f, pivot = center) {
                val a = baseRadius * aMult
                val b = baseRadius * bMult

                // Elipse orbital discontinua de alta definición
                val ovalRect = androidx.compose.ui.geometry.Rect(
                    left = center.x - a,
                    top = center.y - b,
                    right = center.x + a,
                    bottom = center.y + b
                )

                drawOval(
                    brush = Brush.sweepGradient(
                        listOf(
                            primaryColor.copy(alpha = 0.15f * intensity),
                            glowColor.copy(alpha = 0.55f * intensity),
                            secondaryColor.copy(alpha = 0.2f * intensity),
                            primaryColor.copy(alpha = 0.15f * intensity)
                        ),
                        center = center
                    ),
                    topLeft = ovalRect.topLeft,
                    size = ovalRect.size,
                    style = Stroke(
                        width = 1.2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(16f, 12f, 6f, 12f),
                            phase = orbitRotation * (index + 1)
                        )
                    )
                )

                // Micro-nodo cuántico en trayectoria (un punto diminuto de 2.5dp con estela)
                val nodeAngle = particlePhase * (if (index % 2 == 0) 1f else -1.2f) + (index * 2.1f)
                val nodeX = center.x + a * cos(nodeAngle)
                val nodeY = center.y + b * sin(nodeAngle)
                val nodePos = Offset(nodeX, nodeY)

                // Halo sutil del micro-nodo (radio pequeño, nunca grotesco)
                drawCircle(
                    color = glowColor.copy(alpha = 0.45f * intensity),
                    center = nodePos,
                    radius = 4.5.dp.toPx()
                )
                // Núcleo luminoso fino
                drawCircle(
                    color = Color.White.copy(alpha = 0.95f),
                    center = nodePos,
                    radius = 1.8.dp.toPx()
                )

                // Micro-estela de probabilidad (3 puntos decrecientes)
                for (tail in 1..3) {
                    val tailAngle = nodeAngle - (tail * 0.08f * (if (index % 2 == 0) 1f else -1.2f))
                    val tailPos = Offset(center.x + a * cos(tailAngle), center.y + b * sin(tailAngle))
                    drawCircle(
                        color = secondaryColor.copy(alpha = (0.4f / tail) * intensity),
                        center = tailPos,
                        radius = (1.5.dp / tail).toPx()
                    )
                }
            }
        }
    }
}
