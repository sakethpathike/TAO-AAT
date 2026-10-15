package com.sakethh.tao_aat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sakethh.tao_aat.di.aatViewModel
import com.sakethh.tao_aat.ui.navigation.BottomNavBar
import com.sakethh.tao_aat.ui.navigation.Navigation
import com.sakethh.tao_aat.ui.screens.events.EventQueueScreen
import com.sakethh.tao_aat.ui.screens.events.EventQueueScreenVM
import com.sakethh.tao_aat.ui.screens.statistics.StatisticsScreen
import com.sakethh.tao_aat.ui.screens.statistics.StatisticsScreenState
import com.sakethh.tao_aat.ui.theme.TAOAATTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navBackStack = rememberNavBackStack(Navigation.Queue)
            TAOAATTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    Column(modifier = Modifier.navigationBarsPadding()) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                        BottomNavBar(selectedNavKey = navBackStack.last(), onNavigate = { navKey ->
                            if (navBackStack.last() == navKey) return@BottomNavBar
                            navBackStack.add(navKey)
                        })
                    }
                }) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = navBackStack,
                        onBack = {
                            navBackStack.removeLastOrNull()
                        },
                        entryProvider = entryProvider {
                            entry<Navigation.Queue> {
                                val eventQueueScreenVM = aatViewModel<EventQueueScreenVM>()
                                val eventQueueScreenState by eventQueueScreenVM.eventQueueScreenState.collectAsStateWithLifecycle()
                                EventQueueScreen(
                                    onReloadAttributes = {
                                        eventQueueScreenVM.dropAndAddAttributeRows()
                                    },
                                    eventQueueScreenState = eventQueueScreenState
                                )
                            }

                            entry<Navigation.Statistics> {
                                StatisticsScreen(
                                    statisticsScreenState = StatisticsScreenState(
                                        stats = emptyList(),
                                        totalEventsProcessed = 0,
                                        totalUniqueVisits = 0
                                    )
                                )
                            }
                        })
                }
            }
        }
    }
}