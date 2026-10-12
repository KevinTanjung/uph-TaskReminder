package edu.uph.learn.taskreminder.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMint

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val navItems = listOf(
    BottomNavItem("dashboard", "Home", Icons.Default.Home),
    BottomNavItem("calendar", "Calendar", Icons.Default.CalendarMonth),
    BottomNavItem("profile", "Profile", Icons.Default.Person)
)

@Composable
fun TaskBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColdBackground)
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 64.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(top = 6.dp, start = 6.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val selected = currentRoute == item.route
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(interactionSource = interactionSource, indication = null) { onNavigate(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .fillMaxWidth()
                            .background(if (selected) SoftMint else Color.Transparent)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = true, color = SoftMint)
                            ) { onNavigate(item.route) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selected) PrimaryTeal else Ink,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected) PrimaryTeal else Ink
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(52.dp),
            shape = CircleShape,
            color = PrimaryTeal,
            shadowElevation = 6.dp,
            onClick = onAddClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create assignment",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
