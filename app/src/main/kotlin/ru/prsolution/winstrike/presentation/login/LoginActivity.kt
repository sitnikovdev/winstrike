package ru.prsolution.winstrike.presentation.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.toolbar.*
import ru.prsolution.winstrike.R

/*
 * Created by oleg on 31.01.2018.
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ru.prsolution.winstrike.R.layout.ac_login)


//        Navigation
        navController = Navigation.findNavController(this@LoginActivity, R.id.login_host_fragment)

        navController.addOnDestinationChangedListener { nav, destination, _ ->
/*            when (destination.id) {
                R.id.navigation_home -> {bottomNavigation.show()
                    destination.label = PrefUtils.arenaName
                }
                R.id.navigation_order,
                R.id.navigation_profile -> bottomNavigation.show()
                else -> bottomNavigation.hide()
            }*/
        }

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(toolbar)
        setupActionBar(navController, appBarConfiguration)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


}
