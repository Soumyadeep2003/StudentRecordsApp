package com.example.studentrecordsapp.ui.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.studentrecordsapp.R
import com.example.studentrecordsapp.database.Student
import com.example.studentrecordsapp.ui.main.HomePage
import com.example.studentrecordsapp.viewmodel.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditStudentActivity : AppCompatActivity() {
    private val studentViewModel: StudentViewModel by viewModels()
    private var studentId: Int = -1
    private lateinit var etStudentDOB: EditText
    private lateinit var etStudentAge: EditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        val etStudentName: EditText = findViewById(R.id.etStudentName)
        val etStudentDepartment: EditText = findViewById(R.id.etStudentDepartment)
        etStudentAge = findViewById(R.id.etStudentAge)
        val etStudentMarks: EditText = findViewById(R.id.etStudentMarks)
        val etStudentFatherName: EditText = findViewById(R.id.etStudentFatherName)
        etStudentDOB = findViewById(R.id.etStudentDOB)
        val etStudentAddress: EditText = findViewById(R.id.etStudentAddress)
        val etStudentPhone: EditText = findViewById(R.id.etStudentPhone)

        val btnUpdate: Button = findViewById(R.id.btnUpdate)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        etStudentAge.isFocusable = false
        etStudentAge.isClickable = false

        val student = intent.getParcelableExtra<Student>("student")
        student?.let {
            studentId = it.id
            etStudentName.setText(it.name)
            etStudentDepartment.setText(it.department)
            etStudentAge.setText(it.age.toString())
            etStudentMarks.setText(it.marks.toString())
            etStudentFatherName.setText(it.fathersName)
            etStudentDOB.setText(it.dateOfBirth)
            etStudentAddress.setText(it.address)
            etStudentPhone.setText(it.phoneNumber)
        }

        etStudentDOB.setOnClickListener { showDatePicker() }

        btnUpdate.setOnClickListener {
            val name = etStudentName.text.toString().trim()
            val department = etStudentDepartment.text.toString().trim()
            val dob = etStudentDOB.text.toString().trim()
            val age = etStudentAge.text.toString().toIntOrNull() ?: 0
            val marks = etStudentMarks.text.toString().toIntOrNull() ?: -1
            val fathersName = etStudentFatherName.text.toString().trim()
            val address = etStudentAddress.text.toString().trim()
            val phoneNumber = etStudentPhone.text.toString().trim()

            val updatedStudent = Student(
                id = studentId,
                name = name,
                department = department,
                age = age,
                marks = marks,
                fathersName = fathersName,
                dateOfBirth = dob,
                address = address,
                phoneNumber = phoneNumber
            )

            studentViewModel.updateStudent(updatedStudent)
        }

        btnCancel.setOnClickListener {
            navigateToHomePage()
        }

        studentViewModel.validationError.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Student updated successfully!", Toast.LENGTH_SHORT).show()
                navigateToHomePage()
            }
        })
    }

    private fun showDatePicker() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                if (selectedDate.after(Calendar.getInstance())) {
                    Toast.makeText(this, "Future dates are not allowed!", Toast.LENGTH_SHORT).show()
                } else {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    etStudentDOB.setText(dateFormat.format(selectedDate.time))
                    etStudentAge.setText(calculateAge(selectedDate).toString())
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun calculateAge(dobCalendar: Calendar): Int {
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
        if (today.get(Calendar.MONTH) < dobCalendar.get(Calendar.MONTH) ||
            (today.get(Calendar.MONTH) == dobCalendar.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) < dobCalendar.get(Calendar.DAY_OF_MONTH))
        ) {
            age--
        }
        return age
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
