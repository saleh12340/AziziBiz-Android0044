package com.mruraza.ims.Utils.Screens

//
//import android.graphics.RenderEffect
//import android.graphics.Shader
//import android.os.Build
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.asComposeRenderEffect
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.unit.dp
//
//fun Modifier.glassEffect(
//    blurRadius: Float = 20f,
//    cornerRadius: Int = 24,
//    overlayColor: Color = Color.White.copy(alpha = 0.2f),
//    isSelected: Boolean = false
//): Modifier = composed {
//    val shape = RoundedCornerShape(cornerRadius.dp)
//    this
//        .clip(shape)
//        .background(
//            if (isSelected) overlayColor.copy(alpha = 0.4f)
//            else overlayColor
//        )
//        .then(
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    Modifier.graphicsLayer(
//                        renderEffect = RenderEffect.createBlurEffect(25f,25f, Shader.TileMode.MIRROR).asComposeRenderEffect()
//                    )
//            } else Modifier
//        )
//        .border(
//            width = if (isSelected) 2.dp else 1.dp,
//            color = if (isSelected) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.3f),
//            shape = shape
//        )
//}

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassEffect(
    blurRadius: Float = 20f,
    cornerRadius: Dp = 20.dp,
    overlayColor: Color = Color.White.copy(alpha = 0.15f),
    borderColor: Color = Color.White.copy(alpha = 0.3f)
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)

    return this
        .then(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Modifier.graphicsLayer {
                    renderEffect = RenderEffect.createBlurEffect(
                        blurRadius, blurRadius, Shader.TileMode.CLAMP
                    ).asComposeRenderEffect()
                    clip = true
                    this.shape = shape
                }
            } else {
                Modifier // no blur, fallback to just overlay
            }
        )
        .background(overlayColor, shape)
        .border(1.dp, borderColor, shape)
}

