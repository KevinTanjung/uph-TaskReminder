package edu.uph.learn.taskreminder.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.uph.learn.taskreminder.ui.theme.CoralAccent
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMint
import edu.uph.learn.taskreminder.ui.theme.WarmBackground
import java.time.ZoneId

private enum class AssignmentFilter {
    TODAY,
    THIS_WEEK,
    ALL_TASKS
}

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onProfileClick: () -> Unit = {},
    onAssignmentClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedFilter by remember {
        mutableStateOf(AssignmentFilter.TODAY)
    }

    val assignments = uiState.assignments
    val now = java.time.LocalDate.now()
    val todayStart = now.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    val tomorrowStart = now.plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    val weekEnd = now.plusDays(7)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val dueToday = assignments.count { it.dueDate >= todayStart && it.dueDate < tomorrowStart }
    val dueSoon = assignments.count { it.dueDate >= todayStart && it.dueDate < weekEnd  }

    // Replace this with your actual completion property.
    val completed = 0

    val visibleAssignments = when (selectedFilter) {
        AssignmentFilter.TODAY -> assignments.filter { it.dueDate >= todayStart && it.dueDate < tomorrowStart }
        AssignmentFilter.THIS_WEEK -> assignments.filter { it.dueDate >= todayStart && it.dueDate < weekEnd }
        AssignmentFilter.ALL_TASKS -> assignments
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        // Greeting and profile
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = greetingForCurrentTime(),
                    fontSize = 14.sp,
                    color = MutedText
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Let's get things done",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            }

            Surface(
                onClick = onProfileClick,
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = PrimaryTeal.copy(alpha = 0.12f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "User profile",
                    tint = PrimaryTeal,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Weekly summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = PrimaryTeal
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "YOUR WEEK AT A GLANCE",
                    fontSize = 10.sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "${assignments.size} assignments",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "$dueSoon due soon · $completed completed",
                    fontSize = 12.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = {
                        if (assignments.isEmpty()) 0f
                        else completed.toFloat() / assignments.size
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                    color = CoralAccent,
                    trackColor = Color(0xFF6B9694),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Coming up section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Coming up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterPill(
                        text = "Today",
                        selected = selectedFilter == AssignmentFilter.TODAY,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedFilter = AssignmentFilter.TODAY
                    }

                    FilterPill(
                        text = "This Week",
                        selected = selectedFilter == AssignmentFilter.THIS_WEEK,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedFilter = AssignmentFilter.THIS_WEEK
                    }

                    FilterPill(
                        text = "All Tasks",
                        selected = selectedFilter == AssignmentFilter.ALL_TASKS,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedFilter = AssignmentFilter.ALL_TASKS
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryTeal)
                }
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Unable to load assignments",
                    color = Danger,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            visibleAssignments.isEmpty() -> {
                Text(
                    text = when (selectedFilter) {
                        AssignmentFilter.TODAY ->
                            "No assignments due today."

                        AssignmentFilter.THIS_WEEK ->
                            "No assignments due this week."

                        AssignmentFilter.ALL_TASKS ->
                            "No assignments yet."
                    },
                    color = MutedText,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = visibleAssignments,
                        key = { it.id }
                    ) { assignment ->
                        DashboardAssignmentCard(
                            title = assignment.title,
                            course = assignment.course,
                            dueLabel = java.text.SimpleDateFormat(
                                "dd MMM yyyy",
                                java.util.Locale.getDefault()
                            ).format(java.util.Date(assignment.dueDate)),
                            dueTimeLabel = java.text.SimpleDateFormat(
                                "HH:mm",
                                java.util.Locale.getDefault()
                            ).format(java.util.Date(assignment.dueDate)),
                            status = DashboardAssignmentStatus.UPCOMING,
                            onClick = {
                                onAssignmentClick(assignment.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPill(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(28.dp),
        shape = RoundedCornerShape(50),
        color = if (selected) SoftMint else Color.Transparent
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
                color = if (selected) PrimaryTeal else MutedText
            )
        }
    }
}

private fun greetingForCurrentTime(): String {
    return when (java.time.LocalTime.now().hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
}
