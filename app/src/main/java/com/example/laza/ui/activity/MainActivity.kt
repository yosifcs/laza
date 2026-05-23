package com.example.laza.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.laza.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val botNavBar = findViewById<BottomNavigationView>(R.id.navigationBar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment),
            drawerLayout
        )

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController

        botNavBar.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        botNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> navController.navigate(R.id.homeFragment)
                R.id.favourite -> navController.navigate(R.id.loginFragment)
            }
            true
        }

        // Control visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.boardingFragment -> {
                    toolbar.visibility = View.GONE
                    botNavBar.visibility = View.GONE
                }

                R.id.loginOrRegisterFragment, R.id.signUp, R.id.loginFragment, R.id.forgotPasswordFragment, R.id.verficationCodeFragment, R.id.newPassword
                    -> {
                    toolbar.visibility = View.VISIBLE
                    botNavBar.visibility = View.GONE
                }

                else -> {
                    toolbar.visibility = View.VISIBLE
                    botNavBar.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}