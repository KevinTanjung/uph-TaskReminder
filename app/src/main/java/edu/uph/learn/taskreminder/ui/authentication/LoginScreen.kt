package edu.uph.learn.taskreminder.ui.authentication

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
import edu.uph.learn.taskreminder.ui.theme.ColdBackground

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onClickRegister: () -> Unit,
    onClickResetPassword: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(ColdBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Screen")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginSuccess
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClickRegister
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClickResetPassword
        ) {
            Text("Reset Password")
        }
    }
}
