package com.example.avatarframe.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Ultra-Fast Modular Memory Cache for Transparent Avatar Frames:
 * Strips all black backgrounds and inner center holes cleanly with alpha falloff.
 */
object FrameImageCache {
    private val memoryCache = ConcurrentHashMap<Int, ImageBitmap>()

    fun getCachedBitmap(@DrawableRes resId: Int): ImageBitmap? {
        return memoryCache[resId]
    }

    fun processBitmapSynchronous(context: Context, @DrawableRes resId: Int): ImageBitmap {
        memoryCache[resId]?.let { return it }

        try {
            val origBitmap = BitmapFactory.decodeResource(context.resources, resId)
                ?: return ImageBitmap(1, 1)

            val width = origBitmap.width
            val height = origBitmap.height
            val pixels = IntArray(width * height)
            origBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

            val centerX = width / 2f
            val centerY = height / 2f
            val maxRadius = min(width, height) / 2f
            val innerRadius = maxRadius * 0.57f

            val outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for (y in 0 until height) {
                for (x in 0 until width) {
                    val idx = y * width + x
                    val color = pixels[idx]
                    val r = (color shr 16) and 0xFF
                    val g = (color shr 8) and 0xFF
                    val b = color and 0xFF
                    val brightness = max(r, max(g, b))

                    val dx = x - centerX
                    val dy = y - centerY
                    val dist = sqrt(dx * dx + dy * dy)

                    // 1. Center Avatar Window (100% Transparent for avatar portrait)
                    if (dist < innerRadius) {
                        pixels[idx] = 0x00000000
                    }
                    // 2. Full Frame Preservation with Chroma Keying (Ears, horns, wings & staffs extend 100% freely)
                    else {
                        val distToEdge = min(min(x, width - 1 - x), min(y, height - 1 - y))
                        val edgeFade = when {
                            distToEdge < 4 -> (distToEdge / 4f).coerceIn(0f, 1f)
                            else -> 1f
                        }

                        // Smooth Chroma Keying to eliminate all dark background artifacts seamlessly
                        val baseAlpha = when {
                            brightness < 18 -> 0f
                            brightness < 55 -> (brightness - 18) / 37f
                            else -> 1.0f
                        }
                        val finalAlpha = (baseAlpha * edgeFade * 255f).toInt().coerceIn(0, 255)
                        pixels[idx] = (finalAlpha shl 24) or (r shl 16) or (g shl 8) or b
                    }
                }
            }

            outBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            val imageBitmap = outBitmap.asImageBitmap()
            memoryCache[resId] = imageBitmap
            return imageBitmap
        } catch (e: Exception) {
            return ImageBitmap(1, 1)
        }
    }

    suspend fun warmUpAll(context: Context, resIds: List<Int>) = withContext(Dispatchers.Default) {
        for (resId in resIds) {
            processBitmapSynchronous(context, resId)
        }
    }
}
