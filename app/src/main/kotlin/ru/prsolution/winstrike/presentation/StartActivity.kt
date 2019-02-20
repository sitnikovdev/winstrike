package ru.prsolution.winstrike.presentation

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity



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

}


