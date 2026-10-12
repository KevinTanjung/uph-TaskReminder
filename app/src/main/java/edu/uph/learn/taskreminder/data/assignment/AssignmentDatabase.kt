package edu.uph.learn.taskreminder.data.assignment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Assignment::class],
    version = 1,
    exportSchema = true
)
abstract class AssignmentDatabase : RoomDatabase() {

    abstract fun assignmentDao(): AssignmentDao

    companion object {
        @Volatile
        private var INSTANCE: AssignmentDatabase? = null

        fun getInstance(context: Context): AssignmentDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AssignmentDatabase::class.java,
                    "task_reminder.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
