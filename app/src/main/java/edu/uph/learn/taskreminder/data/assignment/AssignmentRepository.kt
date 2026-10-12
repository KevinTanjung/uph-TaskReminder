package edu.uph.learn.taskreminder.data.assignment

class AssignmentRepository(
    private val dao: AssignmentDao
) {
    suspend fun insert(assignment: Assignment) {
        dao.insert(assignment)
    }
}
