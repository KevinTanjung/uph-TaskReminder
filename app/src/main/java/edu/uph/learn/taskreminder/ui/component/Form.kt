package edu.uph.learn.taskreminder.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMint
import edu.uph.learn.taskreminder.ui.theme.interFamily

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    content: (@Composable ColumnScope.() -> Unit)? = null,
    hint: String? = null,
    isPassword: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = Ink,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            fontFamily = interFamily
        )
        if (content != null) {
            content()
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MutedText,
                        fontSize = 14.sp,
                        fontFamily = interFamily
                    )
                },
                textStyle = TextStyle(
                    color = Ink,
                    fontSize = 14.sp,
                    fontFamily = interFamily
                ),
                visualTransformation = if (isPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                isError = errorMessage != null,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = SoftMint,
                    focusedBorderColor = PrimaryTeal,
                    errorBorderColor = Danger,
                    unfocusedTextColor = Ink,
                    focusedTextColor = Ink,
                    unfocusedPlaceholderColor = MutedText,
                    focusedPlaceholderColor = MutedText
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = if (keyboardType == KeyboardType.Email) KeyboardCapitalization.None else KeyboardCapitalization.Unspecified,
                    keyboardType = keyboardType
                )
            )
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Danger,
                    fontSize = 12.sp,
                    fontFamily = interFamily
                )
            } else if (hint != null) {
                Text(
                    text = hint,
                    color = MutedText,
                    fontSize = 11.sp,
                    fontFamily = interFamily
                )
            }
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    content: (@Composable ColumnScope.() -> Unit)? = null,
    hint: String? = null,
    errorMessage: String? = null
) {
    FormField(
        label,
        value,
        onValueChange,
        placeholder,
        content,
        hint,
        true,
        errorMessage
    )
}
