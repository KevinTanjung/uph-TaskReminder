package edu.uph.learn.taskreminder.domain.assignment

object AssignmentValidator {

    fun validateCreate(
        title: String,
        course: String,
        dueDate: Long
    ): Map<AssignmentField, String> {
        val errors = mutableMapOf<AssignmentField, String>()

        if (title.isBlank()) {
            errors[AssignmentField.TITLE] = "Assignment name is required."
        }

        if (course.isBlank()) {
            errors[AssignmentField.COURSE] = "Course or subject is required."
        }

        if (dueDate <= System.currentTimeMillis()) {
            errors[AssignmentField.DUE_DATE] = "Due date and time must be in the future."
        }

        return errors
    }
}
