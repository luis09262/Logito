package com.example.avatarframe.vfx

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🎛️ VfxSelectorBar: Selector de Efectos Procedurales Modulares para Marcos de Avatar.
 * Permite al usuario conmutar en vivo entre cualquier efecto VFX y aplicarlo a cualquier marco.
 */
@Composable
fun VfxSelectorBar(
    selectedVfx: VfxType,
    onVfxSelected: (VfxType) -> Unit,
    activeFrameColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Título de la sección con indicador de modularidad
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "EFECTOS VISUALES & AURA",
                    color = Color(0xFFECEFF1),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(activeFrameColor.copy(alpha = 0.16f))
                        .border(0.8.dp, activeFrameColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "MODULAR",
                        color = activeFrameColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Text(
                text = selectedVfx.displayName,
                color = activeFrameColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Fila horizontal de efectos visuales interactivos
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(VfxType.entries) { vfx ->
                VfxCardChip(
                    vfx = vfx,
                    isSelected = vfx == selectedVfx,
                    activeFrameColor = activeFrameColor,
                    onClick = { onVfxSelected(vfx) }
                )
            }
        }
    }
}

@Composable
private fun VfxCardChip(
    vfx: VfxType,
    isSelected: Boolean,
    activeFrameColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else if (isSelected) 1.03f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "VfxCardScale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) activeFrameColor else Color.White.copy(alpha = 0.08f),
        animationSpec = tween(220),
        label = "VfxBorderColor"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF1A1D24) else Color(0xFF101216),
        animationSpec = tween(220),
        label = "VfxBgColor"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .width(130.dp)
            .height(68.dp)
            .shadow(
                elevation = if (isSelected) 6.dp else 0.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = activeFrameColor.copy(alpha = 0.4f),
                spotColor = activeFrameColor.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 1.4.dp else 0.8.dp,
                brush = Brush.linearGradient(
                    listOf(
                        borderColor.copy(alpha = if (isSelected) 0.95f else 0.25f),
                        borderColor.copy(alpha = if (isSelected) 0.50f else 0.10f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Icono del efecto con halo
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) activeFrameColor.copy(alpha = 0.20f)
                        else Color.White.copy(alpha = 0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = vfx.icon,
                    fontSize = 18.sp
                )
            }

            // Textos del efecto
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = vfx.displayName,
                    color = if (isSelected) Color.White else Color(0xFFB0BEC5),
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = vfx.subtitle,
                    color = if (isSelected) activeFrameColor.copy(alpha = 0.85f) else Color(0xFF607D8B),
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }
        }
    }
}
