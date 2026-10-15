package com.sakethh.tao_aat.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.sakethh.tao_aat.ui.theme.TAOAATTheme

@Composable
fun BottomNavBar(selectedNavKey: NavKey, onNavigate: (NavKey) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Tab(
            modifier = Modifier.weight(1f), selected = true, onClick = {
                onNavigate(Navigation.Queue)
            }) {
            Text(
                color = if (selectedNavKey == Navigation.Queue) Color.Blue.copy(0.5f) else Color.Gray,
                text = "Queue",
                modifier = Modifier.padding(15.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Tab(
            modifier = Modifier.weight(1f), selected = true, onClick = {
                onNavigate(Navigation.Statistics)
            }) {
            Text(
                color = if (selectedNavKey == Navigation.Statistics) Color.Blue.copy(0.75f) else Color.Gray,
                text = "Statistics",
                modifier = Modifier.padding(15.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    TAOAATTheme {
        BottomNavBar(
            selectedNavKey = Navigation.Queue, onNavigate = {})
    }
}