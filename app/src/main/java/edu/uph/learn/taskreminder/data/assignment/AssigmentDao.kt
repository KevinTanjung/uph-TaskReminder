package edu.uph.learn.taskreminder.data.assignment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(assignment: Assignment)
}
