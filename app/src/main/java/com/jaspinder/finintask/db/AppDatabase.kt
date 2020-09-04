package com.jaspinder.finintask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaspinder.finintask.data.EmployeeEntity

@Database(entities = [EmployeeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun employeeDao(): IEmployessDb

    companion object
    {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }
        /* start the room db*/
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
            AppDatabase::class.java,  "employees.db")
            .build()
    }
}