package com.jscode.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.jscode.weatherapp.databinding.ActivityMainBinding
import com.jscode.weatherapp.repository.Repository
import com.jscode.weatherapp.repository.db.WeatherDatabase
import com.jscode.weatherapp.ui.MainViewModel
import com.jscode.weatherapp.ui.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val database by lazy { WeatherDatabase.getDatabase(this) }
    private val repository by lazy { Repository(database.weatherDao()) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        viewModel.snack.observe(this) {
            if(it.toString()!="") Snackbar.make(this,binding.root,it, Snackbar.LENGTH_SHORT).show()
        }

    }
}