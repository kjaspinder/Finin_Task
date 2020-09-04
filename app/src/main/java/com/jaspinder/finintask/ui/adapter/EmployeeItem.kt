package com.jaspinder.finintask.ui.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.jaspinder.finintask.R
import com.jaspinder.finintask.data.EmployeeEntity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_employee.*

class EmployeeItem(
    val context: Context,
    val employeeEntity: EmployeeEntity
): Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            /* setting first name last name and email */
            name.text = employeeEntity.first_name+" "+ employeeEntity.last_name
            email.text = employeeEntity.email
            Glide.with(this.containerView).load(employeeEntity.avatar)
                .placeholder(R.drawable.progress_animation).into(image)
        }
    }

    override fun getLayout()= R.layout.item_employee
}
