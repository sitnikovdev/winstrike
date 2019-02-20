package ru.prsolution.winstrike.presentation

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.Navigation


/**
 * Created by Oleg Sitnikov on 2019-02-18
 */

open class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Set fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(ru.prsolution.winstrike.R.layout.ac_splash)

        // Init Koin modules
        injectFeature()

    }

    // Fragment navigation
    fun navigate(action: NavDirections) {
        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.splash_host_fragment).navigate(action)
    }

}


