package com.example.studentrecordsapp.repository

import androidx.lifecycle.LiveData
import com.example.studentrecordsapp.database.Student
import com.example.studentrecordsapp.database.StudentDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(private val studentDao: StudentDao) {
    val allStudents: LiveData<List<Student>> = studentDao.getAllStudents()

    suspend fun insertStudent(student: Student) {
        studentDao.insert(student)
    }

    suspend fun deleteAllStudents() {
        studentDao.deleteAll()
    }

    // ✅ Add this function to delete a single student
    suspend fun deleteStudent(student: Student) {
        studentDao.delete(student)
    }

    suspend fun updateStudent(student: Student) {
        studentDao.update(student)
    }
}