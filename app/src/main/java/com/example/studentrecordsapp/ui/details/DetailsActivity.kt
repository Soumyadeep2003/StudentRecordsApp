package com.example.studentrecordsapp.ui.details

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
class DetailsActivity : AppCompatActivity() {
    private val studentViewModel: StudentViewModel by viewModels()
    private lateinit var etDob: EditText
    private lateinit var etAge: EditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val etName: EditText = findViewById(R.id.nameEditText)
        val etDepartment: EditText = findViewById(R.id.departmentEditText)
        etAge = findViewById(R.id.ageEditText)
        etAge.isEnabled = false // Disable manual age editing
        val etMarks: EditText = findViewById(R.id.marksEditText)
        val etFathersName: EditText = findViewById(R.id.fathersNameEditText)
        etDob = findViewById(R.id.dobEditText)
        val etAddress: EditText = findViewById(R.id.addressEditText)
        val etPhoneNumber: EditText = findViewById(R.id.phoneNumberEditText)

        val btnSave: Button = findViewById(R.id.saveButton)
        val btnViewRecords: Button = findViewById(R.id.viewRecordsButton)

        etDob.setOnClickListener { showDatePicker() }


        studentViewModel.validationError.observe(this, Observer { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Student Saved!", Toast.LENGTH_SHORT).show()
                clearFields(etName, etDepartment, etAge, etMarks, etFathersName, etDob, etAddress, etPhoneNumber)
            }
        })

        btnSave.setOnClickListener {
            val student = Student(
                id = 0, // New student entry
                name = etName.text.toString().trim(),
                department = etDepartment.text.toString().trim(),
                age = etAge.text.toString().toIntOrNull() ?: 0,
                marks = etMarks.text.toString().toIntOrNull() ?: -1,
                fathersName = etFathersName.text.toString().trim(),
                dateOfBirth = etDob.text.toString().trim(),
                address = etAddress.text.toString().trim(),
                phoneNumber = etPhoneNumber.text.toString().trim()
            )

            studentViewModel.insertStudent(student) // Validation is handled in ViewModel
        }

        btnViewRecords.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }

                if (selectedDate.after(Calendar.getInstance())) {
                    Toast.makeText(this, "Future dates are not allowed!", Toast.LENGTH_SHORT).show()
                } else {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    etDob.setText(dateFormat.format(selectedDate.time))
                    etAge.setText(calculateAge(selectedDate).toString())
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
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

        return if (age < 3) {
            Toast.makeText(this, "Student must be at least 3 years old!", Toast.LENGTH_SHORT).show()
            0
        } else {
            age
        }
    }

    private fun clearFields(vararg fields: EditText) {
        fields.forEach { it.text.clear() }
    }
}
