package com.example.ui.components.frame

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.avatarframe.cache.FrameImageCache
import com.example.avatarframe.ui.DivineAvatarFrame
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EnergyGold

/**
 * Backwards-compatible delegator to the modular DivineAvatarFrame SDK
 */
@Composable
fun DivineFrameCore(
    modifier: Modifier = Modifier,
    @DrawableRes frameDrawableRes: Int = R.drawable.frame_celestial_gold_1789374233057,
    @DrawableRes avatarDrawableRes: Int = R.drawable.avatar_celestial_mage_1789374278416,
    gemColor: Color = ElectricBlue,
    goldTrimColor: Color = EnergyGold,
    onAddClick: () -> Unit = {}
) {
    DivineAvatarFrame(
        modifier = modifier,
        frameRes = frameDrawableRes,
        avatarRes = avatarDrawableRes,
        showNeuralVfx = false,
        enable3DParallax = false,
        showActionButton = true,
        onActionClick = onAddClick
    )
}
