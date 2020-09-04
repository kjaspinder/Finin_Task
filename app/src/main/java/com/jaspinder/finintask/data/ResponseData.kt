package com.jaspinder.finintask.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



data class ResponseData (

    @SerializedName("page")
    val page : Int,
    @SerializedName("per_page")
    val per_page:Int,
    @SerializedName("total")
    val total:Int,
    @SerializedName("total_pages")
    val total_pages:Int,
    @SerializedName("data")
    val data: List<EmployeeEntity>

)