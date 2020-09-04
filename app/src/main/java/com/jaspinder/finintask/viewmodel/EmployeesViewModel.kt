package com.jaspinder.finintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaspinder.finintask.data.EmployeeEntity
import com.jaspinder.finintask.data.repository.IEmployeesDataRepository
import com.jaspinder.finintask.utils.internal.lazyDeferred

class EmployeesViewModel(
    private val employeeRepository: IEmployeesDataRepository

) : ViewModel() {


    lateinit var employeedata : LiveData<List<EmployeeEntity>>
    var page : Int = 1

    suspend fun getData(forceNetwork: Boolean){

        employeedata  = employeeRepository.getEmployeesData(page, forceNetwork)
    }


    val fetchError by lazyDeferred {employeeRepository.getEmployeesFetchError()}
}