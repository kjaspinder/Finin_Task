package com.jaspinder.finintask.network

import androidx.lifecycle.LiveData
import com.jaspinder.finintask.data.EmployeeEntity

interface IEmployeeDataSource {

    val downloadedEmployeeData : LiveData<List<EmployeeEntity>>

    suspend fun fetchData(page : Int, forceNetwork: Boolean)
}