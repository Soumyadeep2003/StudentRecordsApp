package com.example.studentrecordsapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    @Query("SELECT * FROM students ORDER BY id ASC")
    fun getAllStudents(): LiveData<List<Student>>

    @Query("DELETE FROM students")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(student: Student)

    @Update
    suspend fun update(student: Student)
}