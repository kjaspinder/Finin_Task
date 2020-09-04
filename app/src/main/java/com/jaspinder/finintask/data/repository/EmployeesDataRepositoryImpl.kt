package com.jaspinder.finintask.data.repository

import android.content.Context
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jaspinder.finintask.EmployeesApplication
import com.jaspinder.finintask.data.EmployeeEntity
import com.jaspinder.finintask.db.IEmployessDb
import com.jaspinder.finintask.network.ConnectivityInterceptorImpl
import com.jaspinder.finintask.network.EmployeeDataSourceImpl
import com.jaspinder.finintask.network.IEmployeeNetworkService
import com.jaspinder.finintask.utils.Constants
import kotlinx.coroutines.*



class EmployeesDataRepositoryImpl(
    context: Context,

    private val employeesdao: IEmployessDb
) : IEmployeesDataRepository {

    private val dataSourceImpl =  EmployeeDataSourceImpl()
    private val connectivityInterceptor = ConnectivityInterceptorImpl(context)

    init {

        dataSourceImpl.downloadedEmployeeData.observeForever { newEmployeesData ->

            if(newEmployeesData == null)return@observeForever

            saveEmployeeDataInDb(newEmployeesData)


        }
    }

    private fun saveEmployeeDataInDb(fetchedData: kotlin.collections.List<EmployeeEntity>){
        GlobalScope.launch(Dispatchers.IO) {
            employeesdao.insertEmployee(fetchedData)
        }
    }



    private suspend fun initData(page : Int, forceNetwork: Boolean)
    {
        Log.d(Constants.TAG,"getDataFromNetwork $page")
        if((isNetworkFetchNeeded(page) || forceNetwork) && connectivityInterceptor.isOnline())
        {
            getDataFromNetwork(page, forceNetwork)
        }else{
            if(employeesdao.storedEmployeesCount() == 0){
                dataSourceImpl._error.postValue(true)
                getEmployeesFetchError()
            }

        }
    }

    private fun isNetworkFetchNeeded(page:Int):Boolean
    {
        Log.d(Constants.TAG,"getDataFromNetwork ${EmployeesApplication.prefHelper.defaultPrefs().getLong(Constants.LAST_FETCH, 1)} ${System.currentTimeMillis()}")
        return employeesdao.storedEmployeesCount() < page * EmployeesApplication.prefHelper.defaultPrefs().getInt(Constants.PAGE_SIZE,1) ||
                System.currentTimeMillis() - EmployeesApplication.prefHelper.defaultPrefs().getLong(Constants.LAST_FETCH, 1) > Constants.FORTYFIVEMIN

    }


    private suspend fun getDataFromNetwork(page:Int, forceNetwork: Boolean)
    {
        Log.d(Constants.TAG,"getDataFromNetwork $page")
        dataSourceImpl.fetchData(page, forceNetwork)
    }

    override  suspend fun getEmployeesData(page : Int, forceNetwork: Boolean): LiveData<kotlin.collections.List<EmployeeEntity>> {
        return withContext(Dispatchers.IO)
        {
            initData(page, forceNetwork)
            return@withContext employeesdao.getAllEmployees()
        }
    }

    override suspend fun getEmployeesFetchError(): LiveData<Boolean> {
        return withContext(Dispatchers.IO)
        {
            return@withContext dataSourceImpl.getErrorData
        }
    }


}