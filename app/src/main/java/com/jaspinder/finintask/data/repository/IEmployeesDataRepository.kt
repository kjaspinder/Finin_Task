package com.jaspinder.finintask.data.repository



import androidx.lifecycle.LiveData;
import com.jaspinder.finintask.data.EmployeeEntity



public interface IEmployeesDataRepository {

    suspend fun getEmployeesData(page:Int, forceNetwork: Boolean) : LiveData<List<EmployeeEntity>>

    suspend fun getEmployeesFetchError() : LiveData<Boolean>
}