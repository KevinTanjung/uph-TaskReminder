package edu.uph.learn.taskreminder.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.uph.learn.taskreminder.ui.theme.WarmBackground

@Composable
fun DashboardScreen(
    onLogout: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Dashboard")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout
            ) {
                Text("Log Out")
            }
        }
    }
}
