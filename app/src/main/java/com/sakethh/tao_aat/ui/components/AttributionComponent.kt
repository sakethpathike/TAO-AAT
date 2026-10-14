package com.sakethh.tao_aat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.ui.asColor
import com.sakethh.tao_aat.ui.theme.TAOAATTheme

@Composable
fun AttributionComponent(modifier: Modifier, attribution: Attribution) {
    Card(
        shape = RoundedCornerShape(15.dp), modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(attribution.type.asColor(), shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(text = attribution.type.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = attribution.insertedAt.toString())
                }
            }
            Text(
                text = attribution.status.name,
                color = attribution.status.asColor(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
@Preview
private fun AttributionComponentPreview() {
    TAOAATTheme {
        AttributionComponent(
            modifier = Modifier.fillMaxWidth(), attribution = Attribution(
                type = Attribution.Type.ADD_TO_CART,
                status = Attribution.Status.PROCESSING,
                insertedAt = 25252525252,
                sessionId = "sfsdfsdfdsf"
            )
        )
    }
}