package com.jaspinder.finintask.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jaspinder.finintask.data.EmployeeEntity
import com.jaspinder.finintask.data.ResponseData
import com.jaspinder.finintask.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface IEmployeeNetworkService {
    //https://reqres.in/api/users?page=1&delay=3

    @GET(Constants.EMPLOYEES)
    fun getEmployees(@Query("page") page : Int): Call<ResponseData>




}