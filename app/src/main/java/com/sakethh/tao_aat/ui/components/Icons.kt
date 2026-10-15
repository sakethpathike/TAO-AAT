package com.sakethh.tao_aat.ui.components
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {

    val Reload: ImageVector
        get() {
            if (_ArrowCounterclockwise != null) {
                return _ArrowCounterclockwise!!
            }
            _ArrowCounterclockwise = ImageVector.Builder(
                name = "ArrowCounterclockwise",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(8f, 3f)
                    arcToRelative(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4.546f, 2.914f)
                    arcToRelative(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.908f, -0.417f)
                    arcTo(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = false, 8f, 2f)
                    close()
                }
                path(fill = SolidColor(Color.Black)) {
                    moveTo(8f, 4.466f)
                    verticalLineTo(0.534f)
                    arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.41f, -0.192f)
                    lineTo(5.23f, 2.308f)
                    arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 0.384f)
                    lineToRelative(2.36f, 1.966f)
                    arcTo(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8f, 4.466f)
                }
            }.build()

            return _ArrowCounterclockwise!!
        }

    @Suppress("ObjectPropertyName")
    private var _ArrowCounterclockwise: ImageVector? = null

}