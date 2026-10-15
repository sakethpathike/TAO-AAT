package com.sakethh.tao_aat.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey

@Composable
fun BottomNavBar(onNavigate: (NavKey) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Tab(
            modifier = Modifier.weight(1f), selected = true, onClick = {
                onNavigate(Navigation.Queue)
            }) {
            Text(text = "Queue")
        }
        Tab(
            modifier = Modifier.weight(1f), selected = true, onClick = {
                onNavigate(Navigation.Statistics)
            }) {
            Text(text = "Statistics")
        }
    }
}