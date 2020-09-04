package com.jaspinder.finintask.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jaspinder.finintask.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavController()
        setupToolbar()
    }

    private fun setupNavController() {
        navController = findNavController(R.id.nav_host_frag)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController = null
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        navController?.let { navController ->
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
        }
    }
}