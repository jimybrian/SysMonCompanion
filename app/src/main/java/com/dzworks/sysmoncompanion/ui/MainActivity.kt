package com.dzworks.sysmoncompanion.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dzworks.sysmoncompanion.R
import com.dzworks.sysmoncompanion.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        installSplashScreen()
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        setSupportActionBar(binding.tlBar)
        val myNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val inflater = myNavHostFragment.navController.navInflater
        val navController = myNavHostFragment.navController

        navController.graph = inflater.inflate(R.navigation.navigation_main)

        val navGraph = navController.graph

        val appBarConfiguration = AppBarConfiguration(navGraph)
        binding.tlBar.setupWithNavController(navController, appBarConfiguration)
    }
}