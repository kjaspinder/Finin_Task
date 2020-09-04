package com.jaspinder.finintask.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "employee_data")
data class EmployeeEntity (
    @PrimaryKey
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
) : Parcelable