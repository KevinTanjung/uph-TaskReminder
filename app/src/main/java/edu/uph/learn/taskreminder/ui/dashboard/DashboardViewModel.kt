package edu.uph.learn.taskreminder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uph.learn.taskreminder.data.assignment.Assignment
import edu.uph.learn.taskreminder.data.assignment.AssignmentService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val assignments: List<Assignment> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class DashboardViewModel(
    service: AssignmentService
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> =
        service.observeAssignmentsForCurrentUser()
            .map { assignments ->
                DashboardUiState(
                    assignments = assignments,
                    isLoading = false
                )
            }
            .catch { exception ->
                emit(
                    DashboardUiState(
                        isLoading = false,
                        error = exception.message
                            ?: "Unable to load assignments."
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DashboardUiState()
            )
}
