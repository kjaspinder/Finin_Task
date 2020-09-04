package com.jaspinder.finintask.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager


import androidx.recyclerview.widget.RecyclerView
import com.jaspinder.finintask.EmployeesApplication
import com.jaspinder.finintask.R
import com.jaspinder.finintask.data.EmployeeEntity
import com.jaspinder.finintask.network.ConnectivityInterceptorImpl
import com.jaspinder.finintask.ui.adapter.EmployeeItem
import com.jaspinder.finintask.utils.Constants
import com.jaspinder.finintask.utils.EmployeesViewModelFactory
import com.jaspinder.finintask.viewmodel.EmployeesViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_employees_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class EmployeesListFragment : BaseFragment(), KodeinAware {

    override val kodein by closestKodein()
    private var loading : Boolean = true
    private val viewModelFactory: EmployeesViewModelFactory by instance()
    private lateinit var groupAdapter: GroupAdapter<ViewHolder>
    private lateinit var employeesViewModel: EmployeesViewModel
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employees_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        employeesViewModel = ViewModelProviders.of(this, viewModelFactory).get(EmployeesViewModel::class.java)

        getEmployeesData(false)

        handleSwipeRefresh()


    }

    /* swipe from top to force fetch from network */
    private fun handleSwipeRefresh(){
        val connectivityInterceptor = ConnectivityInterceptorImpl(EmployeesApplication.appContext)
        swipe_refresh.setOnRefreshListener{
            if(!connectivityInterceptor.isOnline()){
                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }else{
                employeesViewModel.page = 1
                getEmployeesData(true)

            }
            swipe_refresh.isRefreshing = false
        }


    }

    /* listening to data change */
    private fun getEmployeesData(forceNetwork : Boolean) = launch {
        employeesViewModel.getData(forceNetwork)
        val data = employeesViewModel.employeedata
        val errorInfo = employeesViewModel.fetchError.await()

        data.observe(viewLifecycleOwner, Observer { it ->

            if (it == null || it.isEmpty()) return@Observer
            if(recycler_browse.adapter !=null){
                refreshList(it.toEmployeeItem())
            }else{
                showEmployeeList(it)
            }

        })

        errorInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    showErrorState()
                }

            }
        })

    }

    /* reflect changes in recycler view list */
    private fun refreshList(items: List<EmployeeItem>){
        groupAdapter.update(items)
        recycler_browse.adapter!!.notifyDataSetChanged()
        loading = false
    }

    /* go to error screen if internet is not available */
    private fun showErrorState() {
        findNavController().navigate(R.id.action_ListFragment_to_FailureFragment)
    }

    /* initialize recycler view */
    private fun showEmployeeList(employeedatas: List<EmployeeEntity>) = launch(Dispatchers.Main)
    {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Employees"
        initRecyclerView(employeedatas.toEmployeeItem())

    }

    private fun List<EmployeeEntity>.toEmployeeItem(): List<EmployeeItem> {
        return this.map {
            EmployeeItem(requireContext(), it)
        }

    }

    private fun initRecyclerView(items: List<EmployeeItem>) {
        groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)

        }





        recycler_browse.apply {
            linearLayoutManager =
                LinearLayoutManager(this@EmployeesListFragment.context)
            adapter = groupAdapter
        }

        /* handle paging on scroll*/
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if(dy > 0){

                    val visibleItemCount: Int = recycler_browse.childCount
                    val totalItemCount: Int = recycler_browse.adapter!!.itemCount
                    val pastVisiblesItems: Int = (recycler_browse.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if(loading){
                        if(visibleItemCount + pastVisiblesItems >= totalItemCount){
                            Log.d(Constants.TAG," $visibleItemCount $totalItemCount $pastVisiblesItems")
                            val page_size = EmployeesApplication.prefHelper.defaultPrefs().getInt(Constants.PAGE_SIZE,1)
                            val total_size = EmployeesApplication.prefHelper.defaultPrefs().getInt(Constants.TOTAL_EMPLOYEES,1)
                            employeesViewModel.page = (totalItemCount / page_size) + 1
                            if(totalItemCount < total_size){
                                getEmployeesData(false)
                            }



                        }
                    }

                }
            }
        }
        recycler_browse.addOnScrollListener(scrollListener)



        groupAdapter.setOnItemClickListener{item , view ->
            (item as? EmployeeItem)?.let {
                showEmployeeDetails(item.employeeEntity)
            }
        }




    }

    private fun showEmployeeDetails(model : EmployeeEntity)
    {


        val actionBrowseFragmentToDetailsFragment = EmployeesListFragmentDirections.actionListFragmentToDetailFragment2(model)
        findNavController().navigate(actionBrowseFragmentToDetailsFragment)

    }

}