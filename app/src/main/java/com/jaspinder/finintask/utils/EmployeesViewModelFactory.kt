package com.jaspinder.finintask.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.jaspinder.finintask.data.repository.IEmployeesDataRepository
import com.jaspinder.finintask.viewmodel.EmployeesViewModel

class EmployeesViewModelFactory(

    private val employeesRepository: IEmployeesDataRepository

) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EmployeesViewModel(employeesRepository) as T
    }
}