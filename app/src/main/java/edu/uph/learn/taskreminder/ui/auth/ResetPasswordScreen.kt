package edu.uph.learn.taskreminder.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import edu.uph.learn.taskreminder.data.auth.AuthService
import edu.uph.learn.taskreminder.domain.auth.AuthField
import edu.uph.learn.taskreminder.domain.auth.AuthFormValidator
import edu.uph.learn.taskreminder.ui.component.FormField
import edu.uph.learn.taskreminder.ui.component.ExternalHeader
import edu.uph.learn.taskreminder.ui.component.PrimaryButton
import edu.uph.learn.taskreminder.ui.component.SubtextButton
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMint
import edu.uph.learn.taskreminder.ui.theme.interFamily
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    onBackToLogin: () -> Unit
) {
    // Form Fields
    var email by remember { mutableStateOf("") }

    // Form State
    var fieldErrors by remember { mutableStateOf(mapOf<AuthField, String>()) }
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
        ExternalHeader()

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
                fieldErrors = fieldErrors - AuthField.EMAIL
            },
            placeholder = "you@example.com",
            errorMessage = fieldErrors[AuthField.EMAIL],
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(16.dp))

        // Action Section
        errorMessage?.let { message ->
            Text(
                text = message,
                color = Danger,
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

                fieldErrors = AuthFormValidator.validateResetPassword(email)
                errorMessage = null
                if (fieldErrors.isNotEmpty()) {
                    return@PrimaryButton
                }

                coroutineScope.launch {
                    isSending = true
                    errorMessage = null
                    successMessage = null

                    try {
                        AuthService.sendPasswordResetEmail(email)
                        successMessage = "If an account exists for this email, password-reset " +
                                "instructions may be sent. Check your inbox and spam folder."
                    } catch (e: FirebaseNetworkException) {
                        errorMessage = "Network error. Please try again."
                    } catch (e: FirebaseAuthException) {
                        errorMessage = "Unable to process the request. Please check the email " +
                                "and try again."
                    } catch (e: Exception) {
                        errorMessage = "Something went wrong. Please try again."
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
