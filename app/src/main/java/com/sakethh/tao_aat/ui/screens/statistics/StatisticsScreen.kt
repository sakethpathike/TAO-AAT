package com.sakethh.tao_aat.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.ui.components.StatisticComponent
import com.sakethh.tao_aat.ui.components.TrackerScaffold
import com.sakethh.tao_aat.ui.theme.TAOAATTheme

@Composable
fun StatisticsScreen(statisticsScreenState: StatisticsScreenState) {
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
                items(statisticsScreenState.stats) { stat ->
                    StatisticComponent(
                        modifier = Modifier.fillMaxWidth(),
                        stat = stat,
                    )
                }
            }
        }
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
                        TypeStat(type = Attribution.Type.INSTALL, count = 1),
                        TypeStat(type = Attribution.Type.VISIT, count = 3),
                        TypeStat(type = Attribution.Type.ADD_TO_CART, count = 2),
                        TypeStat(type = Attribution.Type.PURCHASE, count = 1),
                        TypeStat(type = Attribution.Type.INSTALL, count = 0),
                        TypeStat(type = Attribution.Type.VISIT, count = 0),
                        TypeStat(type = Attribution.Type.ADD_TO_CART, count = 0),
                        TypeStat(type = Attribution.Type.PURCHASE, count = 0),
                        TypeStat(type = Attribution.Type.INSTALL, count = 5),
                        TypeStat(type = Attribution.Type.VISIT, count = 12),
                        TypeStat(type = Attribution.Type.ADD_TO_CART, count = 7),
                        TypeStat(type = Attribution.Type.PURCHASE, count = 3),
                        TypeStat(type = Attribution.Type.INSTALL, count = 100),
                        TypeStat(type = Attribution.Type.VISIT, count = 250),
                        TypeStat(type = Attribution.Type.PURCHASE, count = 40)
                    )
                }, totalEventsProcessed = 25, totalUniqueVisits = 60
            )
        )
    }
}