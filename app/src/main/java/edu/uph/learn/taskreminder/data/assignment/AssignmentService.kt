package edu.uph.learn.taskreminder.data.assignment

import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class AssignmentService(
    private val repository: AssignmentRepository,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun createAssignment(
        title: String,
        course: String,
        description: String,
        dueDate: Long
    ) {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("You must be signed in.")

        val now = System.currentTimeMillis()

        val assignment = Assignment(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = title.trim(),
            course = course.trim(),
            description = description.trim(),
            dueDate = dueDate,
            updatedAt = now
        )

        repository.insert(assignment)
    }
}
