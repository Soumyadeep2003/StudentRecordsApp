package com.example.studentrecordsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentrecordsapp.database.Student
import com.example.studentrecordsapp.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(private val repository: StudentRepository) : ViewModel() {

    val allStudents: LiveData<List<Student>> = repository.allStudents

    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> get() = _validationError

    fun insertStudent(student: Student) {
        if (!isValidStudent(student)) return
        viewModelScope.launch {
            repository.insertStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        if (!isValidStudent(student)) return
        viewModelScope.launch {
            repository.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        repository.deleteStudent(student)
    }

    fun removeAllStudents() = viewModelScope.launch {
        repository.deleteAllStudents()
    }

    private fun isValidStudent(student: Student): Boolean {
        return when {
            !isValidName(student.name) -> {
                _validationError.value = "Invalid name! Name should only contain letters and be at least 3 characters long."
                false
            }
            !isValidName(student.fathersName) -> {
                _validationError.value = "Invalid father's name! It should only contain letters and be at least 3 characters long."
                false
            }
            !isValidDepartment(student.department) -> {
                _validationError.value = "Invalid department! It must contain only letters."
                false
            }
            student.dateOfBirth.isEmpty() -> {
                _validationError.value = "Please select a valid Date of Birth."
                false
            }
            student.age < 3 -> {
                _validationError.value = "Student must be at least 3 years old!"
                false
            }
            student.marks !in 0..100 -> {
                _validationError.value = "Marks must be between 0 and 100."
                false
            }
            student.address.isEmpty() -> {
                _validationError.value = "Address cannot be empty."
                false
            }
            !isValidPhone(student.phoneNumber) -> {
                _validationError.value = "Enter a valid 10-digit phone number."
                false
            }
            else -> {
                _validationError.value = null
                true
            }
        }
    }

    private fun isValidName(name: String) = name.matches(Regex("^[A-Za-z ]{3,}$"))
    private fun isValidDepartment(department: String) = department.matches(Regex("^[A-Za-z ]{2,}$"))
    private fun isValidPhone(phone: String) = phone.matches(Regex("^\\d{10}$"))
}
