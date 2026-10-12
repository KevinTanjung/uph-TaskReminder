package edu.uph.learn.taskreminder.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import edu.uph.learn.taskreminder.data.auth.AuthService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EditPasswordViewModel : ViewModel() {
    private val _isEditingPassword = MutableStateFlow(false)
    val isEditingPassword = _isEditingPassword.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _events = Channel<EditPasswordUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun editPassword(
        oldPassword: String,
        newPassword: String
    ) {
        if (_isEditingPassword.value) return

        viewModelScope.launch {
            _isEditingPassword.value = true
            _errorMessage.value = null

            try {
                AuthService.editPassword(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
                _events.send(EditPasswordUiEvent.PasswordUpdated)
            } catch (e: FirebaseNetworkException) {
                Log.e("EditPasswordScreen", "FirebaseNetworkException on edit password: ${e.message}", e)
                _errorMessage.value = "Network error. Please try again."
            } catch (e: FirebaseAuthException) {
                Log.e("EditPasswordScreen", "FirebaseAuthException on edit password: ${e.message}", e)
                _errorMessage.value = "Edit password failed. Please check your current " +
                        "password and try again."
            } catch (e: Exception) {
                Log.e("EditPasswordScreen", "Exception on edit password: ${e.message}", e)
                _errorMessage.value = "Something went wrong. Please try again."
            } finally {
                _isEditingPassword.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
