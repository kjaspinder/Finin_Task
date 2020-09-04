package com.jaspinder.finintask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jaspinder.finintask.R
import kotlinx.android.synthetic.main.fragment_employee_details.*
import kotlinx.android.synthetic.main.item_employee.view.*
import kotlinx.android.synthetic.main.item_employee.view.name

class EmployeeDetailFragment : Fragment() {

    private val args: EmployeeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = args.employeeDetail.first_name

        val employee_detail = args.employeeDetail

        name.text = "${employee_detail.first_name} ${employee_detail.last_name}"

        email.text = employee_detail.email

        context?.applicationContext?.let { context ->
            Glide.with(context).load(employee_detail.avatar)
                .placeholder(R.drawable.progress_animation).into(image)
        }
    }
}