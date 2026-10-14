package com.sakethh.tao_aat.ui.screens

import androidx.compose.runtime.Immutable
import com.sakethh.tao_aat.domain.Attribution

@Immutable
data class EventQueueScreenState(
    val allEvents: List<Attribution>,
    val retryingEvents: List<Attribution>
)
