package edu.uph.learn.taskreminder.ui.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMintLight
import edu.uph.learn.taskreminder.ui.theme.WarmBackground
import edu.uph.learn.taskreminder.ui.theme.interFamily

@Composable
fun ProfileScreen(
    email: String,
    onClickEditPassword: () -> Unit,
    onClickReminderSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(WarmBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Heading
        Text(
            text = "Profile",
            color = Ink,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = interFamily
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Manage your account and preferences.",
            color = MutedText,
            fontSize = 14.sp,
            fontFamily = interFamily
        )

        Spacer(Modifier.height(24.dp))

        // Account identity
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
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

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Your Account",
                    color = Ink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = interFamily
                )

                Text(
                    text = email,
                    color = MutedText,
                    fontSize = 13.sp,
                    fontFamily = interFamily
                )
            }
        }

        Spacer(Modifier.height(36.dp))

        // Account section
        Text(
            text = "ACCOUNT",
            color = Ink,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFamily
        )

        Spacer(Modifier.height(8.dp))

        ProfileActionTile(
            icon = Icons.Outlined.Lock,
            title = "Edit Password",
            subtitle = "Update your account password",
            onClick = onClickEditPassword
        )

        Spacer(Modifier.height(12.dp))

        ProfileActionTile(
            icon = Icons.Outlined.Notifications,
            title = "Reminder Settings",
            subtitle = "Manage default notifications",
            onClick = onClickReminderSettings
        )

        Spacer(Modifier.height(32.dp))

        // Destructive action
        Surface(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(
                width = 1.dp,
                color = Danger
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Log Out",
                    color = Danger,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = interFamily
                )

                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    tint = Danger,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "You'll need to sign in again to access your assignments.",
            color = MutedText,
            fontSize = 11.sp,
            fontFamily = interFamily,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

@Composable
private fun ProfileActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = SoftMintLight
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryTeal,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = Ink,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = interFamily
                )

                Text(
                    text = subtitle,
                    color = MutedText,
                    fontSize = 12.sp,
                    fontFamily = interFamily
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MutedText,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
