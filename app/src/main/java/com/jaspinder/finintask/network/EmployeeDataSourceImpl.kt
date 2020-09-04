package com.jaspinder.finintask.network

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jaspinder.finintask.EmployeesApplication
import com.jaspinder.finintask.data.EmployeeEntity
import com.jaspinder.finintask.data.ResponseData
import com.jaspinder.finintask.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.time.LocalDateTime

class EmployeeDataSourceImpl(

) : IEmployeeDataSource {


    private val _downloadedEmployeeData = MutableLiveData<List<EmployeeEntity>>()
    val _error = MutableLiveData<Boolean>()


    override val downloadedEmployeeData: LiveData<List<EmployeeEntity>>
        get() = _downloadedEmployeeData

    val getErrorData: LiveData<Boolean>
        get() = _error

    override suspend fun fetchData(page: Int, forceNetwork: Boolean) = try {
        _error.postValue(false)
        Log.e(Constants.TAG,"fetchData $page")
        val request = NetworkService.buildService(IEmployeeNetworkService::class.java)
        val call = request.getEmployees(page)
        call.enqueue(object :Callback<ResponseData>{
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                _error.postValue(true)
            }

            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                Log.e(Constants.TAG, " "+response.body())
                if(response.isSuccessful){

                    val editor = EmployeesApplication.prefHelper.defaultPrefs().edit()
                    editor.putInt(Constants.PAGE_SIZE, response.body()!!.per_page)
                    editor.putInt(Constants.TOTAL_PAGES, response.body()!!.total_pages)
                    editor.putInt(Constants.TOTAL_EMPLOYEES, response.body()!!.total)
                    editor.putLong(Constants.LAST_FETCH, System.currentTimeMillis())
                    editor.apply()
                    _downloadedEmployeeData
                    _downloadedEmployeeData.postValue(response.body()!!.data)
                }else{
                    _error.postValue(true)
                }

            }
        })

    } catch (e: Exception) {
        Log.e("exception_fetching", " "+e.message)
        _error.postValue(true)
    }


}

