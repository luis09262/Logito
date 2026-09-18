package com.example.ui.components.vfx

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.avatarframe.vfx.NeuralSynapseCanvas
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EnergyGold
import com.example.ui.theme.OverchargeCyan

/**
 * Backwards-compatible delegator to modular NeuralSynapseCanvas
 */
@Composable
fun EnergyFireCanvas(
    modifier: Modifier = Modifier,
    frameRadiusFraction: Float = 0.36f,
    primaryColor: Color = EnergyGold,
    secondaryColor: Color = ElectricBlue,
    glowColor: Color = OverchargeCyan,
    intensity: Float = 1.0f,
    isCelestialTheme: Boolean = true
) {
    NeuralSynapseCanvas(
        modifier = modifier,
        frameRadiusFraction = frameRadiusFraction,
        primaryColor = primaryColor,
        secondaryColor = secondaryColor,
        glowColor = glowColor,
        intensity = intensity,
        isCelestialTheme = isCelestialTheme
    )
}
