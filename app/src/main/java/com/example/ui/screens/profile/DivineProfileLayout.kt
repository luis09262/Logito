package com.example.ui.screens.profile

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.avatarframe.cache.FrameImageCache
import com.example.avatarframe.catalog.AvatarFrameCatalog
import com.example.avatarframe.model.ElementalAura
import com.example.avatarframe.model.FrameCategory
import com.example.avatarframe.model.FrameItem
import com.example.avatarframe.model.FrameSpecialFeature
import com.example.avatarframe.ui.DivineAvatarFrame
import com.example.avatarframe.vfx.VfxSelectorBar
import com.example.avatarframe.vfx.VfxType
import com.example.ui.theme.*

/**
 * 👑 DivineProfileLayout: High-End Production Showcase Screen for 3D Avatar Frames & Modular VFX.
 */
@Composable
fun DivineProfileLayout(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allFrames = AvatarFrameCatalog.frames
    val avatars = AvatarFrameCatalog.avatars

    var selectedCategory by remember { mutableStateOf(FrameCategory.ALL) }
    val filteredFrames = remember(selectedCategory) {
        if (selectedCategory == FrameCategory.ALL) allFrames
        else allFrames.filter { it.category == selectedCategory }
    }

    var selectedFrameIndex by remember { mutableIntStateOf(0) }
    var selectedAvatarIndex by remember { mutableIntStateOf(0) }
    var selectedVfx by remember { mutableStateOf(VfxType.ARC_LIGHTNING) }

    // Clamped index for safe access
    val currentFrame = filteredFrames.getOrElse(selectedFrameIndex.coerceIn(0, filteredFrames.size - 1)) { allFrames.first() }
    val currentAvatar = avatars[selectedAvatarIndex]

    // Pre-warm frame textures in memory
    LaunchedEffect(Unit) {
        FrameImageCache.warmUpAll(context, allFrames.map { it.frameRes })
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBase),
        containerColor = ObsidianBase,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. SLEEK MINIMALIST STUDIO HEADER
            item {
                SleekStudioHeader(
                    currentFrame = currentFrame,
                    onInfo = {
                        Toast.makeText(context, "👑 Estudio de Avatar 3D - Efectos VFX Modulares Procedurales", Toast.LENGTH_SHORT).show()
                    },
                    onShare = {
                        Toast.makeText(context, "📤 Perfil '${currentFrame.name}' listo para compartir", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // 2. HERO AVATAR SHOWCASE STAGE
            item {
                DivineAvatarFrame(
                    modifier = Modifier.testTag("avatar_hero_stage"),
                    frameItem = currentFrame,
                    avatarRes = currentAvatar.avatarRes,
                    frameSize = 320.dp,
                    vfxType = selectedVfx,
                    enable3DParallax = true,
                    showActionButton = true,
                    onActionClick = {
                        selectedAvatarIndex = (selectedAvatarIndex + 1) % avatars.size
                        Toast.makeText(context, "Avatar: ${avatars[selectedAvatarIndex].name}", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // 3. FRAME TITLE & TIER BADGE
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = currentFrame.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.2.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TitaniumSurface)
                            .border(1.dp, currentFrame.primaryColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentFrame.tier,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentFrame.primaryColor,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // 4. DECOUPLED PROCEDURAL VFX AURA SELECTOR (APPLIED TO ANY FRAME)
            item {
                VfxSelectorBar(
                    selectedVfx = selectedVfx,
                    onVfxSelected = { vfx ->
                        selectedVfx = vfx
                        Toast.makeText(context, "✨ Efecto VFX Equipado: ${vfx.displayName}", Toast.LENGTH_SHORT).show()
                    },
                    activeFrameColor = currentFrame.primaryColor
                )
            }

            // 5. SOCIAL STATS & ATTRIBUTES
            item {
                ProfileHeroAttributesRow(
                    primaryColor = currentFrame.primaryColor,
                    secondaryColor = currentFrame.secondaryColor
                )
            }

            // 6. CATEGORY FILTER CHIPS
            item {
                CategorySelectorBar(
                    selectedCategory = selectedCategory,
                    onCategorySelected = {
                        selectedCategory = it
                        selectedFrameIndex = 0
                    }
                )
            }

            // 7. HORIZONTAL FRAME CAROUSEL PICKER
            item {
                FrameSelectionCarousel(
                    frames = filteredFrames,
                    selectedIndex = selectedFrameIndex.coerceIn(0, filteredFrames.size - 1),
                    onFrameSelected = { selectedFrameIndex = it }
                )
            }

            // 8. ACTION BUTTONS (EQUIP / SAVE) WITH AAA GAMING TEXTURES
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón 1: CAMBIAR FOTO (Titanium Cyber-Optic Texture)
                    GameUiActionButton(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("switch_avatar_button"),
                        textureRes = R.drawable.btn_change_photo_bg_1789635843356,
                        contentDescription = "Cambiar Foto de Avatar",
                        onClick = {
                            selectedAvatarIndex = (selectedAvatarIndex + 1) % avatars.size
                        }
                    )

                    // Botón 2: EQUIPAR MARCO (Divine Gold / Radiant Energy Crystal Texture)
                    GameUiActionButton(
                        modifier = Modifier
                            .weight(1.25f)
                            .testTag("equip_frame_button"),
                        textureRes = R.drawable.btn_equip_frame_bg_1789635864301,
                        contentDescription = "Equipar Marco Seleccionado",
                        onClick = {
                            Toast.makeText(
                                context,
                                "✨ ¡Marco '${currentFrame.name}' Equipado con Éxito!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

/**
 * 👑 SleekStudioHeader: Luxury Minimalist Avatar Studio Header
 */
@Composable
private fun SleekStudioHeader(
    currentFrame: FrameItem,
    onInfo: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Clean Studio Identity
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TitaniumSurface)
                    .border(1.dp, TitaniumBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👑",
                    fontSize = 16.sp
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "ESTUDIO DE AVATAR",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "VFX MODULAR & MARCOS 3D",
                    color = currentFrame.primaryColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        }

        // Right: Clean Minimalist Actions
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onInfo,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TitaniumSurface)
                    .border(1.dp, TitaniumBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Studio Info",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = onShare,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TitaniumSurface)
                    .border(1.dp, TitaniumBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir Perfil",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Profile Hero Attributes Row
 */
@Composable
private fun ProfileHeroAttributesRow(
    primaryColor: Color,
    secondaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatPill(
            title = "NIVEL DIVINO",
            value = "LVL 99",
            accentColor = primaryColor,
            modifier = Modifier.weight(1f)
        )
        StatPill(
            title = "RANGO GLOBAL",
            value = "ARCHON I",
            accentColor = secondaryColor,
            modifier = Modifier.weight(1f)
        )
        StatPill(
            title = "PODER 3D",
            value = "98.4K",
            accentColor = EnergyGold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatPill(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(TitaniumSurface)
            .border(1.dp, TitaniumBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = TextTertiary,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
        }
    }
}

/**
 * Category Selector Bar
 */
@Composable
private fun CategorySelectorBar(
    selectedCategory: FrameCategory,
    onCategorySelected: (FrameCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FrameCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) OverchargeCyan else TitaniumSurface)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) OverchargeCyan else TitaniumBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = category.title,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                    color = if (isSelected) Color.Black else TextSecondary,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Frame Selection Carousel
 */
@Composable
private fun FrameSelectionCarousel(
    frames: List<FrameItem>,
    selectedIndex: Int,
    onFrameSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(frames) { index, item ->
            val isSelected = index == selectedIndex
            FrameMiniatureCard(
                item = item,
                isSelected = isSelected,
                onClick = { onFrameSelected(index) }
            )
        }
    }
}

@Composable
private fun FrameMiniatureCard(
    item: FrameItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .width(86.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ObsidianElevated else TitaniumSurface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) item.primaryColor else TitaniumBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.frameRes),
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = item.name.split(" - ").last().take(10),
                fontSize = 9.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) TextPrimary else TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) =
    androidx.compose.foundation.BorderStroke(width, color)

/**
 * 🎮 GameUiActionButton: AAA Gaming Textured Button with Tactile Physics & Clean Borderless Art
 */
@Composable
private fun GameUiActionButton(
    modifier: Modifier = Modifier,
    @androidx.annotation.DrawableRes textureRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "ButtonScale"
    )

    Box(
        modifier = modifier
            .height(58.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = Color.White.copy(alpha = 0.3f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // 1. Textura 3D Limpia del Botón (Puro arte, sin bordes ni líneas artificiales)
        Image(
            painter = painterResource(id = textureRes),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Micro-brillo interactivo de respuesta al presionar
        if (isPressed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.16f))
            )
        }
    }
}
