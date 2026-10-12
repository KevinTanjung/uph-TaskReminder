package edu.uph.learn.taskreminder.ui.authentication

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import edu.uph.learn.taskreminder.data.auth.AuthService
import edu.uph.learn.taskreminder.ui.component.FormField
import edu.uph.learn.taskreminder.ui.component.Header
import edu.uph.learn.taskreminder.ui.component.PasswordField
import edu.uph.learn.taskreminder.ui.component.PrimaryButton
import edu.uph.learn.taskreminder.ui.component.SubtextButton
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.CoralAccent
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.interFamily
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    // Form Fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    // Form State
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isRegistering by remember { mutableStateOf(false) }

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
            text = "Create Your Account",
            color = Ink,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 27.sp,
            fontFamily = interFamily
        )
        Spacer(Modifier.height(16.dp))
        Text(
            color = MutedText,
            text = "Start organising your assignments today.",
            fontSize = 14.sp,
            fontFamily = interFamily
        )

        Spacer(Modifier.height(24.dp))

        // Form Section
        FormField(
            label = "Email Address",
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            placeholder = "you@example.com"
        )

        Spacer(Modifier.height(24.dp))

        PasswordField(
            label = "Password",
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            placeholder = "Enter your password"
        )

        Spacer(Modifier.height(24.dp))

        PasswordField(
            label = "Confirm Password",
            value = passwordConfirmation,
            onValueChange = {
                passwordConfirmation = it
                errorMessage = null
            },
            placeholder = "Re-enter your password",
            hint = "Both password fields must match."
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

        PrimaryButton(
            label = if (isRegistering) "Creating Account..." else "Create Account",
            onClick = {
                if (isRegistering) {
                    return@PrimaryButton
                }

                coroutineScope.launch {
                    isRegistering = true
                    errorMessage = null

                    try {
                        val user = AuthService.register(email, password);
                        Log.d("RegisterScreen", "User registered: ${user.uid}")
                        onRegisterSuccess();
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Exception on register: ${e.message}", e)
                        errorMessage = when (e) {
                            is FirebaseNetworkException -> "Network error. Please try again."
                            is FirebaseAuthException -> "Registration failed. Please check your details and try again."
                            else -> "Something went wrong. Please try again."
                        }
                    } finally {
                        isRegistering = false
                    }
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        SubtextButton("Log In", onBackToLogin, "Already have an account?")
    }
}
