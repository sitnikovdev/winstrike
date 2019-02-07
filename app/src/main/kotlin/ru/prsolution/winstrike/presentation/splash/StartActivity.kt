package ru.prsolution.winstrike.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

/*
 * Created by oleg on 31.01.2018.
 */

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        val splashActivity = Intent(this, SplashActivity::class.java)
        startActivity(splashActivity)
    }
}
