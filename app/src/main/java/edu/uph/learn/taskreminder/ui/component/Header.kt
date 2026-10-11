package edu.uph.learn.taskreminder.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.uph.learn.taskreminder.R
import edu.uph.learn.taskreminder.ui.theme.interFamily

@Composable
fun Header() {
    Row(
        modifier = Modifier.padding(top = 48.dp, bottom = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_small),
            contentDescription = "Task Reminder logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(40.dp).padding(end = 8.dp)
        )

        Text(
            text = "Task Reminder",
            fontFamily = interFamily,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
