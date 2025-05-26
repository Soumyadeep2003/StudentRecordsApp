package com.example.studentrecordsapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentrecordsapp.R
import com.example.studentrecordsapp.database.Student

class StudentAdapter(
    private val onItemClick: (Student) -> Unit,  // Click Card → Open Details
    private val onEditClick: (Student) -> Unit,  // Click Edit Icon → Open EditStudentActivity
    private val onDeleteClick: (Student) -> Unit // Click Delete Icon → Show Confirmation
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }
        }
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentCard: CardView = itemView.findViewById(R.id.studentCard)
        val studentIdText: TextView = itemView.findViewById(R.id.textViewStudentId)
        val nameText: TextView = itemView.findViewById(R.id.textViewName)
        val ageText: TextView = itemView.findViewById(R.id.textViewAge)
        val editIcon: ImageView = itemView.findViewById(R.id.editIcon)   // Edit Button
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon) // Delete Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)

        // ✅ Display Student Details
        holder.studentIdText.text = "ID: ${student.id}"
        holder.nameText.text = student.name
        holder.ageText.text = "Age: ${student.age}"

        // ✅ Click on Card → Open StudentDetailsActivity
        holder.studentCard.setOnClickListener {
            onItemClick(student)
        }

        // ✅ Click Edit Icon → Open EditStudentActivity
        holder.editIcon.setOnClickListener {
            onEditClick(student)
        }

        // ✅ Click Delete Icon → Show Confirmation Dialog
        holder.deleteIcon.setOnClickListener {
            onDeleteClick(student)
        }
    }
}
