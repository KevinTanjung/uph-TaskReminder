package edu.uph.learn.taskreminder.ui.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import edu.uph.learn.taskreminder.ui.component.PasswordField
import edu.uph.learn.taskreminder.ui.component.PrimaryButton
import edu.uph.learn.taskreminder.ui.component.SubtextButton
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.interFamily
import kotlinx.coroutines.launch

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
    var fieldErrors by remember { mutableStateOf(mapOf<AuthField, String>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoggingIn by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(ColdBackground)
            .padding(24.dp)
    ) {
        ExternalHeader()

        // Top Section
        Text(
            text = "Make every deadline",
            color = Ink,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 27.sp,
            fontFamily = interFamily
        )
        Text(
            text = "feel manageable.",
            color = PrimaryTeal,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 27.sp,
            fontFamily = interFamily
        )
        Spacer(Modifier.height(16.dp))
        Text(
            color = MutedText,
            text = "Keep your assignments organised and",
            fontSize = 14.sp,
            fontFamily = interFamily
        )
        Text(
            color = MutedText,
            text = "get a gentle nudge before they're due.",
            fontSize = 14.sp,
            fontFamily = interFamily
        )

        Spacer(Modifier.height(48.dp))

        // Form Section
        FormField(
            label = "Email Address",
            value = email,
            onValueChange = {
                email = it
                fieldErrors = fieldErrors - AuthField.EMAIL
                errorMessage = null
            },
            placeholder = "you@example.com",
            errorMessage = fieldErrors[AuthField.EMAIL],
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(24.dp))

        PasswordField(
            label = "Password",
            value = password,
            onValueChange = {
                password = it
                fieldErrors = fieldErrors - AuthField.PASSWORD
                errorMessage = null
            },
            placeholder = "Enter your password",
            errorMessage = fieldErrors[AuthField.PASSWORD]
        )

        // Action Section
        SubtextButton(
            "Forgot password?",
            onClickResetPassword,
            horizontalArrangement = Arrangement.End
        )

        Spacer(Modifier.height(16.dp))

        errorMessage?.let { message ->
            Text(
                text = message,
                color = Danger,
                fontSize = 14.sp,
                fontFamily = interFamily
            )

            Spacer(Modifier.height(12.dp))
        }

        PrimaryButton(
            if (isLoggingIn) "Logging in..." else "Log In",
            onClick = {
                if (isLoggingIn) {
                    return@PrimaryButton
                }

                fieldErrors = AuthFormValidator.validateLogin(email, password)
                errorMessage = null
                if (fieldErrors.isNotEmpty()) {
                    return@PrimaryButton
                }

                coroutineScope.launch {
                    isLoggingIn = true
                    errorMessage = null

                    try {
                        val user = AuthService.login(email, password)
                        Log.d("LoginScreen", "User logged in: ${user.uid}")
                        onLoginSuccess()
                    } catch (e: FirebaseNetworkException) {
                        errorMessage =  "Network error. Please try again."
                    } catch (e: FirebaseAuthException) {
                        errorMessage = "Login failed. Please check your details and try again."
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Exception on login: ${e.message}", e)
                        errorMessage = "Something went wrong. Please try again."
                    } finally {
                        isLoggingIn = false
                    }
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        SubtextButton("Create an account", onClickRegister, "New here?")
    }
}
