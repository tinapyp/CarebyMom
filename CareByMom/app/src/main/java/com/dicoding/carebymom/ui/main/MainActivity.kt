package com.dicoding.carebymom.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.carebymom.R
import com.dicoding.carebymom.databinding.ActivityMainBinding
import com.dicoding.carebymom.ui.ViewModelFactory
import com.dicoding.carebymom.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->

            if (!user.isLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
        }

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        // Observasi perubahan fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val fragmentTitle = when (destination.id) {
                R.id.navigation_tips -> getString(R.string.mom_s_tips)
                R.id.navigation_health -> getString(R.string.health_care)
                R.id.navigation_profile -> getString(R.string.profile)
                else -> getString(R.string.app_name)
            }

            supportActionBar?.title = fragmentTitle
        }

    }
}