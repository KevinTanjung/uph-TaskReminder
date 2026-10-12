package edu.uph.learn.taskreminder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.uph.learn.taskreminder.data.assignment.AssignmentService

class AssignmentViewModelFactory(
    private val service: AssignmentService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(AssignmentViewModel::class.java)) {
            return AssignmentViewModel(service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
