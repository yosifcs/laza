package com.example.laza.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.laza.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController  // store it

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val botNavBar = findViewById<BottomNavigationView>(R.id.navigationBar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController  // assign once

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.cartFragment,
                R.id.wishlistFragment,
                R.id.paymentFragment
            ),

            drawerLayout
        )

        botNavBar.setupWithNavController(navController)
        toolbar.setupWithNavController(navController, appBarConfiguration)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.boardingFragment, R.id.productDetailsFragment
                    -> {
                    toolbar.visibility = View.GONE
                    botNavBar.visibility = View.GONE
                }

                R.id.loginOrRegisterFragment,
                R.id.signUp,
                R.id.loginFragment,
                R.id.forgotPasswordFragment,
                R.id.verficationCodeFragment,
                R.id.newPassword -> {
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

    // ✅ Now just uses the stored field
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}