package edu.uph.learn.taskreminder.ui.auth

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import edu.uph.learn.taskreminder.data.auth.AuthService
import edu.uph.learn.taskreminder.domain.auth.AuthField
import edu.uph.learn.taskreminder.domain.auth.AuthFormValidator
import edu.uph.learn.taskreminder.ui.component.FormField
import edu.uph.learn.taskreminder.ui.component.InternalHeader
import edu.uph.learn.taskreminder.ui.component.PasswordField
import edu.uph.learn.taskreminder.ui.component.PrimaryButton
import edu.uph.learn.taskreminder.ui.component.SubtextButton
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.interFamily
import kotlinx.coroutines.launch

@Composable
fun EditPasswordScreen(
    onBackToProfile: () -> Unit
) {
    // Form Fields
    var oldPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    // Form State
    var fieldErrors by remember { mutableStateOf(mapOf<AuthField, String>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isEditingPassword by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(ColdBackground)
            .padding(24.dp)
    ) {
        InternalHeader(
            title = "Edit Password",
            subtitle = "Choose a strong password to protect your account.",
            onBackClick = onBackToProfile
        )

        Spacer(Modifier.height(24.dp))

        // Form Section
        PasswordField(
            label = "Current Password",
            value = oldPassword,
            onValueChange = {
                oldPassword = it
                errorMessage = null
                fieldErrors = fieldErrors - AuthField.PASSWORD_OLD
            },
            placeholder = "Enter current password",
            errorMessage = fieldErrors[AuthField.PASSWORD_OLD]
        )

        Spacer(Modifier.height(8.dp))

        PasswordField(
            label = "New Password",
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
                fieldErrors = fieldErrors - AuthField.PASSWORD
            },
            placeholder = "Enter new password",
            errorMessage = fieldErrors[AuthField.PASSWORD]
        )

        Spacer(Modifier.height(8.dp))

        PasswordField(
            label = "Confirm New Password",
            value = passwordConfirmation,
            onValueChange = {
                passwordConfirmation = it
                errorMessage = null
                fieldErrors = fieldErrors - AuthField.PASSWORD_CONFIRMATION
            },
            placeholder = "Re-enter new password",
            hint = "Your new passwords must match.",
            errorMessage = fieldErrors[AuthField.PASSWORD_CONFIRMATION]
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

        PrimaryButton(
            label = if (isEditingPassword) "Saving Password..." else "Save Password",
            onClick = {
                if (isEditingPassword) {
                    return@PrimaryButton
                }

                fieldErrors = AuthFormValidator.validateEditPassword(oldPassword, password, passwordConfirmation)
                errorMessage = null
                if (fieldErrors.isNotEmpty()) {
                    return@PrimaryButton
                }

                coroutineScope.launch {
                    isEditingPassword = true
                    try {
                        val user = AuthService.editPassword(oldPassword, password)
                        Log.d("RegisterScreen", "User password updated: ${user.uid}")
                        onBackToProfile()
                    } catch (e: FirebaseNetworkException) {
                        errorMessage =  "Network error. Please try again."
                    } catch (e: FirebaseAuthException) {
                        errorMessage = "Edit password failed. Please check your current password and try again."
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Exception on edit password: ${e.message}", e)
                        errorMessage = "Something went wrong. Please try again."
                    } finally {
                        isEditingPassword = false
                    }
                }
            }
        )
    }
}
