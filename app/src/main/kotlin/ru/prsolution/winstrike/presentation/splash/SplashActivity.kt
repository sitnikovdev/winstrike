package ru.prsolution.winstrike.presentation.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import org.jetbrains.anko.longToast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.common.utils.AuthUtils
import ru.prsolution.winstrike.mvp.apimodels.Arenas
import ru.prsolution.winstrike.mvp.apimodels.Room
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.ui.guides.GuideActivity
import ru.prsolution.winstrike.ui.login.SignInActivity
import ru.prsolution.winstrike.presentation.main.MainScreenActivity
import timber.log.Timber
import javax.inject.Inject


open class SplashActivity : AppCompatActivity() {

    private var mainIntent: Intent? = null
    lateinit var splashPresenter: SplashPresenter

    @Inject
    lateinit var mService: Service

    private var rooms: List<Room>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WinstrikeApp.getInstance().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.ac_splash)

        val animationView = findViewById<View>(R.id.animation_view) as LottieAnimationView
        animationView.imageAssetsFolder = "images"
        animationView.setAnimation("data.json")
        animationView.repeatCount = 0
        animationView.scale = 1f

        animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                splashPresenter.getActiveArena()
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animationView.playAnimation()

        splashPresenter = createSplashPresenter()
    }

    fun createSplashPresenter(): SplashPresenter {
        return SplashPresenter(mService, this)
    }


    private fun isCheckLogin() {
        // If user is signOut from App: go to SingIn screen. Else: check if user is exist on server and if Ok -- go to Main screen.
        if (AuthUtils.isLogout) {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        } else if (!AuthUtils.token.isEmpty()) {
            mainIntent = Intent(this@SplashActivity, MainScreenActivity::class.java)
            startActivity(mainIntent)
        } else {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        }
    }


    fun onSendSmsSuccess() {
        longToast("CMC успешно отпралена")
    }


    fun onGetArenasResponseSuccess(authResponse: Arenas) {
        Timber.d("Success get map data from server: %s", authResponse)
        /**
         * data for active room pid successfully get from server.
         * save pid and get map for selected time period
         */
        rooms = authResponse.rooms
        WinstrikeApp.getInstance().rooms = rooms

        if (AuthUtils.isFirstLogin) {
            AuthUtils.isFirstLogin = false
            mainIntent = Intent(this@SplashActivity, GuideActivity::class.java)
            startActivity(mainIntent)
        } else {
            isCheckLogin()
        }


    }

}

