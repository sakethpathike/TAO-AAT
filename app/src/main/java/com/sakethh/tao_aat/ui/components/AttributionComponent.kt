package com.sakethh.tao_aat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.ui.asColor
import com.sakethh.tao_aat.ui.epochToReadableDateTime
import com.sakethh.tao_aat.ui.theme.TAOAATTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AttributionComponent(modifier: Modifier, attribution: Attribution) {
    val readableEpoch = retain(attribution.insertedAt) {
        epochToReadableDateTime(attribution.insertedAt)
    }

    var retryTime by rememberSaveable {
        mutableIntStateOf(0)
    }

    LaunchedEffect(attribution.status) {
        if (attribution.status == Attribution.Status.RETRY) {
            withContext(Dispatchers.Default) {
                while (true) {
                    if (!isActive) break

                    retryTime =
                        ((attribution.nextRetryAt?.minus(System.currentTimeMillis()))?.div(1000)
                            ?: 0).toInt()

                    delay(500L.milliseconds)
                }
            }
        }
    }

    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier.border(
            width = 1.dp,
            color = Color.DarkGray,
            shape = CardDefaults.shape
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
                        .background(attribution.type.asColor(alpha = 0.15f), shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(text = attribution.type.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = readableEpoch)
                }
            }
            Text(
                text = if (attribution.status != Attribution.Status.RETRY) attribution.status.name else if (retryTime <= 0) "Retrying..." else "Retrying in ${retryTime}s",
                color = attribution.status.asColor(),
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
                sessionId = retain {
                    UUID.randomUUID().toString()
                })
        )
    }
}