package com.jaspinder.finintask

import android.app.Application
import android.content.Context
import com.jaspinder.finintask.data.repository.EmployeesDataRepositoryImpl
import com.jaspinder.finintask.data.repository.IEmployeesDataRepository
import com.jaspinder.finintask.db.AppDatabase
import com.jaspinder.finintask.utils.EmployeesViewModelFactory
import com.jaspinder.finintask.utils.PreferenceHelper

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class EmployeesApplication :Application (),KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@EmployeesApplication))


        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { instance<AppDatabase>().employeeDao() }
        bind<IEmployeesDataRepository>() with singleton { EmployeesDataRepositoryImpl(instance(),instance()) }
        bind() from provider { EmployeesViewModelFactory(instance()) }
    }



    companion object {
        lateinit var prefHelper: PreferenceHelper
            private set

        lateinit var appContext: Context

    }


    override fun onCreate() {
        super.onCreate()
        EmployeesApplication.prefHelper = PreferenceHelper(this)
        appContext = this
    }





}