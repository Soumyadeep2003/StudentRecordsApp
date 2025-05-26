package com.example.studentrecordsapp.di

import android.content.Context
import androidx.room.Room
import com.example.studentrecordsapp.database.StudentDao
import com.example.studentrecordsapp.database.StudentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StudentDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StudentDatabase::class.java,
            "student_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStudentDao(database: StudentDatabase): StudentDao {
        return database.studentDao()
    }
}