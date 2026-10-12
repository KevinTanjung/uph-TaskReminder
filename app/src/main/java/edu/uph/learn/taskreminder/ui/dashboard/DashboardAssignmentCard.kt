package edu.uph.learn.taskreminder.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uph.learn.taskreminder.ui.theme.AssignmentCardBorder
import edu.uph.learn.taskreminder.ui.theme.CompletedBackground
import edu.uph.learn.taskreminder.ui.theme.CompletedText
import edu.uph.learn.taskreminder.ui.theme.CoralAccent
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.DueSoon
import edu.uph.learn.taskreminder.ui.theme.DueToday
import edu.uph.learn.taskreminder.ui.theme.DueTodayText
import edu.uph.learn.taskreminder.ui.theme.DueTomorrowText
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.OverdueBackground
import edu.uph.learn.taskreminder.ui.theme.Upcoming
import edu.uph.learn.taskreminder.ui.theme.UpcomingText
import edu.uph.learn.taskreminder.ui.theme.interFamily

private data class StatusStyle(
    val label: String,
    val foreground: Color,
    val background: Color
)

private fun DashboardAssignmentStatus.style(): StatusStyle =
    when (this) {
        DashboardAssignmentStatus.DUE_TODAY -> StatusStyle(
            "Today", DueTodayText, DueToday
        )

        DashboardAssignmentStatus.DUE_TOMORROW -> StatusStyle(
            "Tomorrow", DueTomorrowText, DueSoon
        )

        DashboardAssignmentStatus.UPCOMING -> StatusStyle(
            "Upcoming", UpcomingText, Upcoming
        )

        DashboardAssignmentStatus.OVERDUE -> StatusStyle(
            "Overdue", Danger, OverdueBackground
        )

        DashboardAssignmentStatus.COMPLETED -> StatusStyle(
            "Completed", CompletedText, CompletedBackground
        )
    }

@Composable
fun DashboardAssignmentCard(
    title: String,
    course: String,
    dueLabel: String,
    dueTimeLabel: String,
    status: DashboardAssignmentStatus,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val statusStyle = status.style()
    val completed = status == DashboardAssignmentStatus.COMPLETED

    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(
            1.dp,
            AssignmentCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(74.dp)
                    .background(
                        if (completed) statusStyle.foreground else CoralAccent,
                        CircleShape
                    )
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = Ink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = interFamily,
                    textDecoration = if (completed) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = course,
                    color = MutedText,
                    fontSize = 14.sp,
                    fontFamily = interFamily
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "$dueLabel · $dueTimeLabel",
                    color = MutedText,
                    fontSize = 13.sp,
                    fontFamily = interFamily
                )
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = statusStyle.label,
                color = statusStyle.foreground,
                fontSize = 12.sp,
                fontFamily = interFamily,
                modifier = Modifier
                    .background(
                        statusStyle.background,
                        RoundedCornerShape(50)
                    )
                    .padding(
                        horizontal = 14.dp,
                        vertical = 9.dp
                    )
            )
        }
    }
}
