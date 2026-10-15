package com.sakethh.tao_aat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sakethh.tao_aat.ui.navigation.BottomNavBar
import com.sakethh.tao_aat.ui.navigation.Navigation
import com.sakethh.tao_aat.ui.screens.events.EventQueueScreen
import com.sakethh.tao_aat.ui.screens.events.EventQueueScreenState
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
                    BottomNavBar(onNavigate = { navKey ->
                        if (navBackStack.last() == navKey) return@BottomNavBar
                        navBackStack.add(navKey)
                    })
                }) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = navBackStack,
                        onBack = {
                            navBackStack.removeLastOrNull()
                        },
                        entryProvider = entryProvider {
                            entry<Navigation.Queue> {
                                EventQueueScreen(
                                    eventQueueScreenState = EventQueueScreenState(
                                        allEvents = emptyList(), retryingEvents = emptyList()
                                    )
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