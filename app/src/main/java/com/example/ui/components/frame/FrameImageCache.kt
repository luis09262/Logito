package com.example.ui.components.frame

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
 * Ultra-Fast Thread-Safe In-Memory Cache for Transparent Avatar Frame Bitmaps.
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
            val outerRadius = maxRadius * 0.95f

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

                    // 1. Center Avatar Window (100% Transparent)
                    if (dist < innerRadius) {
                        pixels[idx] = 0x00000000
                    }
                    // 2. Outer Bounds: Clean alpha falloff
                    else if (dist > outerRadius) {
                        val edgeFade = ((dist - outerRadius) / (maxRadius - outerRadius)).coerceIn(0f, 1f)
                        val alpha = ((brightness / 255f) * (1f - edgeFade) * 255).toInt().coerceIn(0, 255)
                        pixels[idx] = (alpha shl 24) or (r shl 16) or (g shl 8) or b
                    }
                    // 3. Smooth Chroma Keying (Zero black background artifacts)
                    else {
                        val alpha = when {
                            brightness < 16 -> 0
                            brightness < 50 -> (((brightness - 16) / 34f) * 255).toInt()
                            else -> 255
                        }
                        pixels[idx] = (alpha shl 24) or (r shl 16) or (g shl 8) or b
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

    suspend fun loadAndCache(context: Context, @DrawableRes resId: Int): ImageBitmap = withContext(Dispatchers.Default) {
        processBitmapSynchronous(context, resId)
    }

    suspend fun warmUpAll(context: Context, resIds: List<Int>) = withContext(Dispatchers.Default) {
        for (resId in resIds) {
            loadAndCache(context, resId)
        }
    }
}
