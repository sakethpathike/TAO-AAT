package com.sakethh.tao_aat.ui

import androidx.compose.ui.graphics.Color
import com.sakethh.tao_aat.domain.Attribution
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Attribution.Type.asColor(alpha: Float = 0.75f): Color {
    return when (this) {
        Attribution.Type.INSTALL -> Color.Green.copy(alpha)
        Attribution.Type.VISIT -> Color.Cyan.copy(alpha)
        Attribution.Type.ADD_TO_CART -> Color.DarkGray.copy(alpha)
        Attribution.Type.PURCHASE -> Color.Yellow.copy(alpha)
    }
}

fun Attribution.Status.asColor(alpha: Float = 0.75f): Color {
    return when (this) {
        Attribution.Status.PROCESSED -> Color.Green.copy(alpha)
        Attribution.Status.PROCESSING -> Color.Blue.copy(alpha)
        Attribution.Status.RETRY -> Color.Yellow.copy(alpha)
    }
}

fun epochToReadableDateTime(
    epochSeconds: Long,
): String = try {
    val date = Date(epochSeconds)
    val formatter = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    formatter.format(date)
} catch (e: Exception) {
    e.printStackTrace()
    epochSeconds.toString()
}