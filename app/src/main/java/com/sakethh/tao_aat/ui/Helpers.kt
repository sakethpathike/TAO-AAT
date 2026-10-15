package com.sakethh.tao_aat.ui

import androidx.compose.runtime.retain.retain
import androidx.compose.ui.graphics.Color
import com.sakethh.tao_aat.domain.Attribution
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Attribution.Type.asColor(alpha: Float): Color {
    return when (this) {
        Attribution.Type.INSTALL -> Color(0xFF16A34A).copy(alpha)
        Attribution.Type.VISIT -> Color(0xFF2563EB).copy(alpha)
        Attribution.Type.ADD_TO_CART -> Color(0xFF6D28D9).copy(alpha)
        Attribution.Type.PURCHASE -> Color(0xFFEA7C1E).copy(alpha)
    }
}

fun Attribution.Status.asColor(): Color {
    return when (this) {
        Attribution.Status.PROCESSED -> Color(0xFF16A34A)
        Attribution.Status.PROCESSING -> Color(0xFF2563EB)
        Attribution.Status.RETRY -> Color(0xFFEA7C1E)
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

val ActiveTabColor = Color(0xFF4F46E5)
val InActiveTabColor = Color(0xFF6B7280)