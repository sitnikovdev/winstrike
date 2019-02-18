package ru.prsolution.winstrike.presentation

import android.animation.Animator
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import kotlinx.android.synthetic.main.fmt_splash.*
import ru.prsolution.winstrike.R

/**
 * Created by Oleg Sitnikov on 2019-02-18
 */

open class StartActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_splash)
        injectFeature()

//        Navigation
        navController = Navigation.findNavController(this@StartActivity, R.id.splash_host_fragment)

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


    }
}


