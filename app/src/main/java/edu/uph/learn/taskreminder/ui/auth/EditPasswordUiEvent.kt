package edu.uph.learn.taskreminder.ui.auth

sealed interface EditPasswordUiEvent {
    data object PasswordUpdated : EditPasswordUiEvent
}
