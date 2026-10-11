package edu.uph.learn.taskreminder.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.uph.learn.taskreminder.ui.component.Header
import edu.uph.learn.taskreminder.ui.component.PrimaryButton
import edu.uph.learn.taskreminder.ui.component.SubtextButton
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.interFamily

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onClickRegister: () -> Unit,
    onClickResetPassword: () -> Unit
) {
    // Form Fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Form State
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(ColdBackground)
            .padding(24.dp)
    ) {
        Header()

        // Top Section
        Text(
            text = "Make every deadline",
            color = Ink,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "feel manageable.",
            color = PrimaryTeal,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(16.dp))

        Text(
            color = MutedText,
            text = "Keep your assignments organised and get a gentle nudge before they're due."
        )

        Spacer(Modifier.height(64.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = "Email Address",
                fontWeight = FontWeight.SemiBold,
                fontFamily = interFamily
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                placeholder = {
                    Text(
                        text = "you@example.com",
                        fontFamily = interFamily
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = "Password",
                fontWeight = FontWeight.SemiBold,
                fontFamily = interFamily
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                placeholder = {
                    Text(
                        text = "Enter your passowrd",
                        fontFamily = interFamily
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))

        SubtextButton("Forgot password?", onClickResetPassword, horizontalArrangement = Arrangement.End)

        Spacer(Modifier.height(16.dp))

        PrimaryButton("Log In", onLoginSuccess)

        Spacer(Modifier.height(16.dp))

        SubtextButton("Create an account", onClickRegister, "New here?")
    }
}
