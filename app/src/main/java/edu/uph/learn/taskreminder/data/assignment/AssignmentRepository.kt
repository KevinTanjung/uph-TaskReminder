package edu.uph.learn.taskreminder.data.assignment

import kotlinx.coroutines.flow.Flow

class AssignmentRepository(
    private val dao: AssignmentDao
) {
    suspend fun insert(assignment: Assignment) {
        dao.insert(assignment)
    }

    fun observeAssignmentByUser(
        userId: String
    ): Flow<List<Assignment>> {
        return dao.observeAssignmentByUser(userId)
    }
}
