package com.example.studentrecordsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentrecordsapp.R
import com.example.studentrecordsapp.database.Student
import com.example.studentrecordsapp.ui.details.StudentDetailsActivity

import com.example.studentrecordsapp.ui.details.DetailsActivity
import com.example.studentrecordsapp.ui.edit.EditStudentActivity
import com.example.studentrecordsapp.viewmodel.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePage : AppCompatActivity() {
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var noRecordsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        studentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val addStudentButton: Button = findViewById(R.id.addStudentButton)
        noRecordsTextView = findViewById(R.id.noRecordsTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Adapter with Click Listeners for Card, Edit, and Delete
        val adapter = StudentAdapter(
            onItemClick = { student -> openStudentDetails(student) },
            onEditClick = { student -> openEditStudent(student) },
            onDeleteClick = { student -> showDeleteConfirmationDialog(student) }
        )
        recyclerView.adapter = adapter

        studentViewModel.allStudents.observe(this) { students ->
            if (students.isEmpty()) {
                noRecordsTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                noRecordsTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.submitList(students)
            }
        }

        // ✅ Add Student Button
        addStudentButton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)

        }
    }

    // ✅ Open StudentDetailsActivity on Card Click
    private fun openStudentDetails(student: Student) {
        val intent = Intent(this, StudentDetailsActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)
    }

    // ✅ Open EditStudentActivity on Edit Button Click
    private fun openEditStudent(student: Student) {
        val intent = Intent(this, EditStudentActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)

    }

    // ✅ Show Delete Confirmation Dialog
    private fun showDeleteConfirmationDialog(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.name}?")
            .setPositiveButton("Delete") { _, _ ->
                studentViewModel.deleteStudent(student)
                Toast.makeText(this, "Student deleted successfully!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
