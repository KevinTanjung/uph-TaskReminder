package edu.uph.learn.taskreminder.data.assignment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    val title: String,
    val course: String,
    val description: String = "",
    @ColumnInfo(name = "due_date")
    val dueDate: Long,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "reminder_time")
    val reminderTime: Long? = null,
    @ColumnInfo(name = "reminder_enabled")
    val reminderEnabled: Boolean = false,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "sync_status")
    val syncStatus: String = "PENDING"
)
