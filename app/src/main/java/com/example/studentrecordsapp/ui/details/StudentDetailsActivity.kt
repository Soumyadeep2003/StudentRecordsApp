package com.example.studentrecordsapp.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studentrecordsapp.R
import com.example.studentrecordsapp.database.Student
import com.example.studentrecordsapp.ui.edit.EditStudentActivity
import com.example.studentrecordsapp.viewmodel.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentDetailsActivity : AppCompatActivity() {
    private lateinit var studentViewModel: StudentViewModel
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

//        val studentIdText: TextView = findViewById(R.id.studentId)
//        val studentIcon: ImageView = findViewById(R.id.studentIcon)
//        val studentName: TextView = findViewById(R.id.studentName)
//        val studentDepartment: TextView = findViewById(R.id.studentDepartment)
//        val studentAge: TextView = findViewById(R.id.studentAge)
//        val studentMarks: TextView = findViewById(R.id.studentMarks)
//        val studentFatherName: TextView = findViewById(R.id.studentFatherName)
//        val studentDOB: TextView = findViewById(R.id.studentDOB)
//        val studentAddress: TextView = findViewById(R.id.studentAddress)
//        val studentPhone: TextView = findViewById(R.id.studentPhone)




        // Get student details from intent
        student = intent.getParcelableExtra("student")
        student?.let {
            updateUI(it)
        }




    }

    // Handle Result from EditStudentActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_STUDENT && resultCode == Activity.RESULT_OK) {
            val updatedStudent = data?.getParcelableExtra<Student>("updatedStudent")
            updatedStudent?.let {
                student = it
                updateUI(it) // Refresh UI with updated data
            }
        }
    }

    private fun updateUI(student: Student) {
        val studentIdText: TextView = findViewById(R.id.studentId)
        val studentName: TextView = findViewById(R.id.studentName)
        val studentDepartment: TextView = findViewById(R.id.studentDepartment)
        val studentAge: TextView = findViewById(R.id.studentAge)
        val studentMarks: TextView = findViewById(R.id.studentMarks)
        val studentFatherName: TextView = findViewById(R.id.studentFatherName)
        val studentDOB: TextView = findViewById(R.id.studentDOB)
        val studentAddress: TextView = findViewById(R.id.studentAddress)
        val studentPhone: TextView = findViewById(R.id.studentPhone)

        studentIdText.text = " ${student.id}" // ✅ Display Student ID
        studentName.text = student.name
        studentDepartment.text = " ${student.department}"
        studentAge.text = " ${student.age}"
        studentMarks.text = " ${student.marks}"
        studentFatherName.text = " ${student.fathersName}"
        studentDOB.text = " ${student.dateOfBirth}"
        studentAddress.text = " ${student.address}"
        studentPhone.text = " ${student.phoneNumber}"
    }

    companion object {
        private const val REQUEST_EDIT_STUDENT = 1
    }
}
