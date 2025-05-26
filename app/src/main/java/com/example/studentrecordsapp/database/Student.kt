package com.example.studentrecordsapp.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val department: String,
    val age: Int,
    val marks: Int,
    val fathersName: String,
    val dateOfBirth: String,
    val address: String,
    val phoneNumber: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(department)
        parcel.writeInt(age)
        parcel.writeInt(marks)
        parcel.writeString(fathersName)
        parcel.writeString(dateOfBirth)
        parcel.writeString(address)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student = Student(parcel)
        override fun newArray(size: Int): Array<Student?> = arrayOfNulls(size)
    }
}