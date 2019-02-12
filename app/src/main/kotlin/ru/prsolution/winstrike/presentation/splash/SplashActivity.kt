package ru.prsolution.winstrike.presentation.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.ac_splash.animation_view
import org.jetbrains.anko.longToast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.login.SignInActivity

open class SplashActivity : AppCompatActivity() {

    private var mainIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.ac_splash)

        mainIntent = Intent(this@SplashActivity, MainActivity::class.java)

        animation_view.imageAssetsFolder = "images"
        animation_view.setAnimation("data.json")
        animation_view.repeatCount = 0
        animation_view.scale = 1f

        animation_view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                openMainActivity()
                finish()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

//        val vm: SplashViewModel = ViewModelProviders.of(this)[SplashViewModel::class.java]

        if (savedInstanceState == null) {
// 			vm.get()
        }
    }

    private fun isCheckLogin() {
        // If user is signOut from App: go to SingIn screen. Else: check if user is exist on server and if Ok -- go to Main screen.
        if (PrefUtils.isLogout) {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        } else if (!PrefUtils.token?.isEmpty()!!) {
            startActivity(mainIntent)
        } else {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        }
    }

    fun onSendSmsSuccess() {
        longToast("CMC успешно отпралена")
    }

    fun openMainActivity() {

        // TODO Use AtomicBoolean
        if (PrefUtils.isFirstLogin && !TextUtils.isEmpty(PrefUtils.token)) {
            PrefUtils.isFirstLogin = false
// 			TODO: Fix guides
// 			mainIntent = Intent(this@SplashActivity, GuideActivity::class.java)
            startActivity(mainIntent)
        } else {
            isCheckLogin()
        }
    }
}
