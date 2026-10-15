package com.sakethh.tao_aat.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.ui.ActiveTabColor
import com.sakethh.tao_aat.ui.InActiveTabColor
import com.sakethh.tao_aat.ui.asColor
import com.sakethh.tao_aat.ui.components.StatisticComponent
import com.sakethh.tao_aat.ui.components.TrackerScaffold
import com.sakethh.tao_aat.ui.theme.TAOAATTheme
import java.util.Locale

@Composable
fun StatisticsScreen(statisticsScreenState: StatisticsScreenState) {
    var totalVisitsHeight by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val localDensity = LocalDensity.current
    TrackerScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Text(text = "Statistics", modifier = Modifier.padding(start = 15.dp, bottom = 15.dp))

            LazyVerticalGrid(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(7.5.dp),
                horizontalArrangement = Arrangement.spacedBy(7.5.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .height(totalVisitsHeight.dp)
                            .border(
                                width = 1.dp, color = Color.DarkGray, shape = CardDefaults.shape
                            ),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier.padding(7.5.dp)
                        ) {
                            Text(text = "Total Events Processed", fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = statisticsScreenState.totalEventsProcessed.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActiveTabColor
                            )
                        }
                    }
                }
                item {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            totalVisitsHeight = with(localDensity) {
                                coordinates.size.height.toDp().value
                            }
                        }
                        .border(
                            width = 1.dp, color = Color.DarkGray, shape = CardDefaults.shape
                        ),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                        Column(modifier = Modifier.padding(7.5.dp)) {
                            Text(
                                text = "Total Visits\n" + "(Unique Session)",
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = statisticsScreenState.totalUniqueVisits.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = ActiveTabColor,
                            )
                        }
                    }
                }
                items(statisticsScreenState.stats) { stat ->
                    StatisticComponent(
                        modifier = Modifier.fillMaxWidth(),
                        stat = stat,
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)) {
                        Text(
                            text = "Event Breakdown",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 10.dp),
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp, color = Color.DarkGray, shape = CardDefaults.shape
                                ),
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                                BreakdownRow(
                                    event = "Event",
                                    count = "Count",
                                    percentage = "Percentage",
                                    fontWeight = FontWeight.SemiBold,
                                    labelColor = InActiveTabColor
                                )
                                HorizontalDivider()
                                statisticsScreenState.stats.forEach { stat ->
                                    key("Event Breakdown$stat") {
                                        val percentage =
                                            if (statisticsScreenState.totalEventsProcessed <= 0) {
                                                "0.00%"
                                            } else {
                                                String.format(
                                                    Locale.getDefault(),
                                                    "%.2f%%",
                                                    stat.count * 100f / statisticsScreenState.totalEventsProcessed,
                                                )
                                            }
                                        BreakdownRow(
                                            event = stat.type.name,
                                            count = stat.count.toString(),
                                            percentage = percentage,
                                            dotColor = stat.type.asColor(1f),
                                            labelColor = Color.Black
                                        )
                                    }
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BreakdownRow(
    event: String,
    count: String,
    percentage: String,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
    labelColor: Color,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = dotColor, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = event, fontSize = 12.sp, fontWeight = fontWeight, color = labelColor
            )
        }
        Text(
            text = count,
            fontSize = 12.sp,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.6f),
            color = labelColor
        )
        Text(
            text = percentage,
            fontSize = 12.sp,
            fontWeight = fontWeight,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.8f),
            color = labelColor
        )
    }
}

@Preview
@Composable
private fun StatisticsScreenPreview() {
    TAOAATTheme {
        StatisticsScreen(
            statisticsScreenState = StatisticsScreenState(
                stats = retain {
                    listOf(
                        TypeStat(type = Attribution.Type.INSTALL, count = 12),
                        TypeStat(type = Attribution.Type.VISIT, count = 26),
                        TypeStat(type = Attribution.Type.ADD_TO_CART, count = 37),
                        TypeStat(type = Attribution.Type.PURCHASE, count = 53),
                    )
                },
                totalEventsProcessed = 128,
                totalUniqueVisits = 26,
            )
        )
    }
}