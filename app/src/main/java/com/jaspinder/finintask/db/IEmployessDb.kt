package com.jaspinder.finintask.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jaspinder.finintask.data.EmployeeEntity

@Dao
interface IEmployessDb {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployee(stores: List<EmployeeEntity>)

    @Query("SELECT * FROM employee_data WHERE id = :storeId ORDER BY id ASC")
    fun getEmployees(storeId: Int): LiveData<List<EmployeeEntity>>

    @Query("SELECT * FROM employee_data ORDER BY id ASC")
    fun getAllEmployees(): LiveData<List<EmployeeEntity>>

    @Query("SELECT COUNT(id) FROM employee_data")
    fun storedEmployeesCount(): Int

    @Query("DELETE FROM employee_data")
    fun deleteEmployeeDataTable()
}