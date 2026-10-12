package edu.uph.learn.taskreminder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uph.learn.taskreminder.data.assignment.AssignmentService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AssignmentViewModel(
    private val service: AssignmentService
) : ViewModel() {

    private val _isCreating = MutableStateFlow(false)
    val isCreating = _isCreating.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun createAssignment(
        title: String,
        course: String,
        description: String,
        dueDate: Long,
        onSaved: () -> Unit
    ) {
        if (_isCreating.value) return

        _isCreating.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                service.createAssignment(
                    title = title,
                    course = course,
                    description = description,
                    dueDate = dueDate
                )
                onSaved()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to save assignment."
            } finally {
                _isCreating.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
