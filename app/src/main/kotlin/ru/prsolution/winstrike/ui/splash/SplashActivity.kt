package ru.prsolution.winstrike.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast

import com.airbnb.lottie.LottieAnimationView
import javax.inject.Inject

import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.common.utils.AuthUtils
import ru.prsolution.winstrike.mvp.apimodels.Arenas
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel
import ru.prsolution.winstrike.mvp.apimodels.Room
import ru.prsolution.winstrike.mvp.models.MessageResponse
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.ui.guides.GuideActivity
import ru.prsolution.winstrike.ui.login.SignInActivity
import ru.prsolution.winstrike.ui.login.UserConfirmActivity
import ru.prsolution.winstrike.ui.main.MainScreenActivity
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

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
        animationView.setImageAssetsFolder("images")
        animationView.setAnimation("data.json")
        animationView.repeatCount = 0
        animationView.scale = 1f

        animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.e("Animation:", "start")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.e("Animation:", "end")
                splashPresenter.getActiveArena()
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.e("Animation:", "cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.e("Animation:", "repeat")
            }
        })
        animationView.playAnimation()

        splashPresenter = createSplashPresenter()
    }

    fun createSplashPresenter(): SplashPresenter {
        return SplashPresenter(mService, this)
    }


    private fun isCheckLogin() {
        // If user is signOut from App:  go to SingIn screen. Else: check if user is exist on server and if Ok -- go to Main screen.
        if (AuthUtils.isLogout) {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        } else if (!AuthUtils.token.isEmpty()) {
            mainIntent = Intent(this@SplashActivity, MainScreenActivity::class.java)
            startActivity(mainIntent)
            //      splashPresenter.getActiveArena();
        } else {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        }
    }


    fun onAuthResponseSuccess(authResponse: AuthResponse) {
        Timber.d("On auth success")
        // Go to main screen
        val confirmed = authResponse.user.confirmed
        AuthUtils.name = authResponse.user.name!!
        AuthUtils.token = authResponse.token
        AuthUtils.phone = authResponse.user.phone!!
        AuthUtils.isConfirmed = authResponse.user.confirmed!!
        AuthUtils.publicid = authResponse.user.publicId!!

        if (confirmed!! && AuthUtils.isLogout != true) {
            splashPresenter.getActiveArena()
            Timber.d("Success signIn")
        } else if (!confirmed) {
            // If user not confirmed: send him sms, and go to UserConfirmActivity
            val smsModel = ConfirmSmsModel()
            smsModel.username = authResponse.user.phone
            splashPresenter.sendSms(smsModel)

            val intent = Intent(this, UserConfirmActivity::class.java)
            intent.putExtra("phone", smsModel.username)
            startActivity(intent)
        }
    }

    fun onAuthFailure(appErrorMessage: String) {
        // Show failure
        Timber.e("Error on auth: %s", appErrorMessage)
        if (appErrorMessage.contains("403")) {
            toast("Неправильный пароль")
        }
        if (appErrorMessage.contains("404")) {
            toast("Пользователь не найден")
        }
        if (appErrorMessage.contains("502")) {
            toast("Ошибка сервера")
        }
        if (appErrorMessage.contains("No Internet Connection!")) {
            toast("Интернет подключение не доступно!")
        }
    }

    fun onSendSmsSuccess(authResponse: MessageResponse) {}

    fun onSendSmsFailure(appErrorMessage: String) {}

    protected fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    fun onGetActivePidFailure(appErrorMessage: String) {
        Timber.d("Failure get map from server: %s", appErrorMessage)
        if (appErrorMessage.contains("502")) {
            toast(getString(R.string.server_error_502))
        } else {
            toast(appErrorMessage)
        }
    }
}

