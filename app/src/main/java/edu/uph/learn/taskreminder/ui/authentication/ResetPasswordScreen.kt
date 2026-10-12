package edu.uph.learn.taskreminder.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import edu.uph.learn.taskreminder.data.auth.AuthService
import edu.uph.learn.taskreminder.ui.component.FormField
import edu.uph.learn.taskreminder.ui.component.Header
import edu.uph.learn.taskreminder.ui.component.PrimaryButton
import edu.uph.learn.taskreminder.ui.component.SubtextButton
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.CoralAccent
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMint
import edu.uph.learn.taskreminder.ui.theme.WarmBackground
import edu.uph.learn.taskreminder.ui.theme.interFamily
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    onBackToLogin: () -> Unit
) {
    // Form Fields
    var email by remember { mutableStateOf("") }

    // Form State
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isSending by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(ColdBackground)
            .padding(24.dp)
    ) {
        Header()

        // Top Section
        Text(
            text = "Reset Password",
            color = Ink,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 27.sp,
            fontFamily = interFamily
        )
        Spacer(Modifier.height(16.dp))
        Text(
            color = MutedText,
            text = "Enter the email associated with your account. We'll send instructions to reset your password.",
            fontSize = 14.sp,
            fontFamily = interFamily
        )

        Spacer(Modifier.height(24.dp))

        FormField(
            label = "Email Address",
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            placeholder = "you@example.com"
        )

        Spacer(Modifier.height(16.dp))

        // Action Section
        errorMessage?.let { message ->
            Text(
                text = message,
                color = CoralAccent,
                fontSize = 14.sp,
                fontFamily = interFamily
            )

            Spacer(Modifier.height(12.dp))
        }

        successMessage?.let { message ->
            Text(
                text = message,
                color = PrimaryTeal,
                fontSize = 14.sp,
                fontFamily = interFamily
            )

            Spacer(Modifier.height(12.dp))
        }

        PrimaryButton(
            label = if (isSending) "Sending Instructions..." else "Send Reset Instructions",
            onClick = {
                if (isSending) {
                    return@PrimaryButton
                }

                coroutineScope.launch {
                    isSending = true
                    errorMessage = null
                    successMessage = null


                    try {
                        AuthService.sendPasswordResetEmail(email)
                        successMessage = "If an account exists for this email, password-reset instructions may be sent. Check your inbox and spam folder."
                    } catch (e: Exception) {
                        errorMessage = when (e) {
                            is FirebaseNetworkException -> "Network error. Please try again."
                            is FirebaseAuthException -> "Unable to process the request. Please check the email and try again."
                            else -> "Something went wrong. Please try again."
                        }
                    } finally {
                        isSending = false
                    }
                }
            }
        )

        Spacer(Modifier.height(48.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = SoftMint
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Check your inbox",
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "If the account exists, password-reset instructions will be sent to this email.",
                    fontSize = 12.sp,
                    color = MutedText,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        SubtextButton("< Back to Log In", onBackToLogin)
    }
}
