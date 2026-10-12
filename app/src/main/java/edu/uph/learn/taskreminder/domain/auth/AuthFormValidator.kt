package edu.uph.learn.taskreminder.domain.auth

import android.util.Patterns

object AuthFormValidator {

    private fun validateEmail(email: String): String? {
        val normalizedEmail = email.trim()

        return when {
            normalizedEmail.isBlank() -> "Email address is required."
            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() -> "Enter a valid email address."
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required."
            else -> null
        }
    }

    fun validateLogin(
        email: String,
        password: String
    ): Map<AuthField, String> {
        val errors = mutableMapOf<AuthField, String>()

        validateEmail(email)?.let {
            errors[AuthField.EMAIL] = it
        }

        validatePassword(password)?.let {
            errors[AuthField.PASSWORD] = it
        }

        return errors
    }

    fun validateRegistration(
        email: String,
        password: String,
        passwordConfirmation: String
    ): Map<AuthField, String> {
        val errors = mutableMapOf<AuthField, String>()

        validateEmail(email)?.let {
            errors[AuthField.EMAIL] = it
        }

        when {
            password.isBlank() -> errors[AuthField.PASSWORD] = "Password is required."
            password.length < 8 -> errors[AuthField.PASSWORD] = "Password must be at least 8 characters."
        }

        when {
            passwordConfirmation.isBlank() -> errors[AuthField.PASSWORD_CONFIRMATION] = "Please confirm your password."
            passwordConfirmation != password -> errors[AuthField.PASSWORD_CONFIRMATION] = "Passwords do not match."
        }

        return errors
    }

    fun validateResetPassword(
        email: String
    ): Map<AuthField, String> {
        val errors = mutableMapOf<AuthField, String>()

        validateEmail(email)?.let {
            errors[AuthField.EMAIL] = it
        }

        return errors
    }

    fun validateEditPassword(
        oldPassword: String,
        newPassword: String,
        passwordConfirmation: String
    ): Map<AuthField, String> {
        val errors = mutableMapOf<AuthField, String>()

        when {
            oldPassword.isBlank() -> errors[AuthField.PASSWORD_OLD] = "Old password is required."
        }

        when {
            newPassword.isBlank() -> errors[AuthField.PASSWORD] = "New password is required."
            newPassword.length < 8 -> errors[AuthField.PASSWORD] = "New password must be at least 8 characters."
        }

        when {
            passwordConfirmation.isBlank() -> errors[AuthField.PASSWORD_CONFIRMATION] = "Please confirm your new password."
            passwordConfirmation != newPassword -> errors[AuthField.PASSWORD_CONFIRMATION] = "New passwords do not match."
        }

        return errors
    }
}
