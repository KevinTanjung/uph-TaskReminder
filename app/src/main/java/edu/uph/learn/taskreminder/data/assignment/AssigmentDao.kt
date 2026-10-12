package edu.uph.learn.taskreminder.data.assignment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(assignment: Assignment)

    @Query(
        """
        SELECT * FROM assignments
        WHERE user_id = :userId
        ORDER BY due_date ASC
        """
    )
    fun observeAssignmentByUser(
        userId: String
    ): Flow<List<Assignment>>
}
