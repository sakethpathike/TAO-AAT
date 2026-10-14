package com.sakethh.tao_aat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sakethh.tao_aat.domain.Attribution
import com.sakethh.tao_aat.ui.components.AttributionComponent
import com.sakethh.tao_aat.ui.components.TrackerScaffold
import com.sakethh.tao_aat.ui.theme.TAOAATTheme
import kotlinx.coroutines.launch

@Composable
fun EventQueueScreen(eventQueueScreenState: EventQueueScreenState) {
    val pagerState = rememberPagerState(
        pageCount = {
            2
        },
    )

    TrackerScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Text(text = "Event Queue", modifier = Modifier.padding(start = 15.dp, bottom = 7.5.dp))
            SecondaryTabRow(
                modifier = Modifier.fillMaxWidth(), selectedTabIndex = pagerState.currentPage
            ) {
                TrackerTab(
                    index = 0, label = "IN PROGRESS", pagerState = pagerState
                )
                TrackerTab(
                    index = 1, label = "FAILED / RETRYING", pagerState = pagerState
                )
            }
            HorizontalPager(state = pagerState) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(7.5.dp)) {
                            item {
                                Spacer(Modifier)
                            }
                            items(eventQueueScreenState.allEvents) { attribution ->
                                AttributionComponent(
                                    modifier = Modifier
                                        .padding(start = 15.dp, end = 15.dp)
                                        .fillMaxWidth(), attribution = attribution
                                )
                            }
                        }
                    }

                    1 -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(7.5.dp)) {
                            item {
                                Spacer(Modifier)
                            }
                            items(eventQueueScreenState.retryingEvents) { attribution ->
                                AttributionComponent(
                                    modifier = Modifier
                                        .padding(start = 15.dp, end = 15.dp)
                                        .fillMaxWidth(), attribution = attribution
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackerTab(index: Int, label: String, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    Tab(
        selected = pagerState.currentPage == index,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }.start()
        },
    ) {
        Text(text = label, modifier = Modifier.padding(12.dp))
    }
}

@Preview
@Composable
private fun EventQueueScreenPreview() {
    TAOAATTheme {
        val events = retain {
            listOf(
                Attribution(
                    type = Attribution.Type.INSTALL,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000000000,
                    sessionId = "sess-01",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.VISIT,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000001000,
                    sessionId = "sess-01",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.ADD_TO_CART,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000002000,
                    sessionId = "sess-01",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.PURCHASE,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000003000,
                    sessionId = "sess-01",
                    nextRetryAt = null
                ),

                Attribution(
                    type = Attribution.Type.VISIT,
                    status = Attribution.Status.RETRY,
                    insertedAt = 1730000004000,
                    sessionId = "sess-02",
                    nextRetryAt = 1730000009000
                ), Attribution(
                    type = Attribution.Type.VISIT,
                    status = Attribution.Status.RETRY,
                    insertedAt = 1730000005000,
                    sessionId = "sess-02",
                    nextRetryAt = 1730000010000
                ), Attribution(
                    type = Attribution.Type.ADD_TO_CART,
                    status = Attribution.Status.PROCESSING,
                    insertedAt = 1730000006000,
                    sessionId = "sess-02",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.PURCHASE,
                    status = Attribution.Status.RETRY,
                    insertedAt = 1730000007000,
                    sessionId = "sess-02",
                    nextRetryAt = 1730000011000
                ),

                Attribution(
                    type = Attribution.Type.INSTALL,
                    status = Attribution.Status.PROCESSING,
                    insertedAt = 1730000008000,
                    sessionId = "sess-03",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.VISIT,
                    status = Attribution.Status.RETRY,
                    insertedAt = 1730000009000,
                    sessionId = "sess-03",
                    nextRetryAt = 1730000012000
                ), Attribution(
                    type = Attribution.Type.ADD_TO_CART,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000010000,
                    sessionId = "sess-03",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.PURCHASE,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000011000,
                    sessionId = "sess-03",
                    nextRetryAt = null
                ),

                Attribution(
                    type = Attribution.Type.VISIT,
                    status = Attribution.Status.PROCESSED,
                    insertedAt = 1730000012000,
                    sessionId = "sess-04",
                    nextRetryAt = null
                ), Attribution(
                    type = Attribution.Type.ADD_TO_CART,
                    status = Attribution.Status.RETRY,
                    insertedAt = 1730000013000,
                    sessionId = "sess-04",
                    nextRetryAt = 1730000018000
                ), Attribution(
                    type = Attribution.Type.PURCHASE,
                    status = Attribution.Status.PROCESSING,
                    insertedAt = 1730000014000,
                    sessionId = "sess-04",
                    nextRetryAt = null
                )
            )
        }
        val retryingEvents = retain {
            events.filter {
                it.status == Attribution.Status.PROCESSING || it.status == Attribution.Status.RETRY
            }
        }
        EventQueueScreen(
            eventQueueScreenState = EventQueueScreenState(
                allEvents = events, retryingEvents = retryingEvents
            )
        )
    }
}