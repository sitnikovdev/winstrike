package ru.prsolution.winstrike.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.ui.splash.SplashActivity
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
