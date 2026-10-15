package com.sakethh.tao_aat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.ui.asColor
import com.sakethh.tao_aat.ui.screens.statistics.TypeStat
import com.sakethh.tao_aat.ui.theme.TAOAATTheme

@Composable
fun StatisticComponent(modifier: Modifier, stat: TypeStat) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = Color.DarkGray,
            shape = CardDefaults.shape
        ),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(7.5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(stat.type.asColor(0.15f), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = stat.type.name, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stat.count.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = stat.type.asColor(alpha = 1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun StatisticComponentPreview() {
    TAOAATTheme {
        StatisticComponent(
            modifier = Modifier, stat = TypeStat(
                type = Attribution.Type.INSTALL, count = 12
            )
        )
    }
}