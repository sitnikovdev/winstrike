package ru.prsolution.winstrike.presentation.login

import android.app.Activity
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.ac_login.et_password
import kotlinx.android.synthetic.main.ac_login.et_phone
import kotlinx.android.synthetic.main.ac_login.tv_register
import kotlinx.android.synthetic.main.ac_login.tv_conditions
import kotlinx.android.synthetic.main.ac_login.login_button
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.TextFormat
import ru.prsolution.winstrike.presentation.utils.Utils.setBtnEnable
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.viewmodel.LoginViewModel
import timber.log.Timber
import android.text.style.ClickableSpan
import android.view.View


/*
 * Created by oleg on 31.01.2018.
 */
// TODO: 13/05/2018 Reorder method call in this activity.
class SignInActivity : AppCompatActivity() {

    private val mVm: LoginViewModel by viewModel()

    private var mProgressDialog: ProgressDialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ru.prsolution.winstrike.R.layout.ac_login)

        if (savedInstanceState == null) {
            mVm.getUser()
        }


        init()

        mVm.authResponse.observe(this@SignInActivity, Observer {
            it.let {
                it?.let { response ->
                    onAuthResponseSuccess(response)
                }
            }
        })
    }

    fun init() {
        TextFormat.formatText(et_phone, Constants.PHONE_MASK)

        setBtnEnable(login_button, true)

        login_button!!.setOnClickListener {
            if (et_phone?.text?.length!! >= Constants.PHONE_LENGTH && et_password?.text?.length!! >= Constants.PASSWORD_LENGTH) {

// 				loginViewModel!!.username = formatPhone(et_phone!!.text.toString())
// 				loginViewModel!!.password = et_password!!.text.toString()

// 				AuthUtils.phone = loginViewModel!!.username.toString()
// 				AuthUtils.password = loginViewModel!!.password.toString()

//                vm.signIn()
            } else if (TextUtils.isEmpty(et_phone!!.text)) {
                longToast("Введите номер телефона")
            } else if (TextUtils.isEmpty(et_password!!.text)) {
                longToast("Пароль не должен быть пустым")
            } else if (et_phone!!.text.length < Constants.PHONE_LENGTH) {
                longToast("Номер телефона не верный")
            } else if (et_password!!.text.length < Constants.PASSWORD_LENGTH) {
                longToast("Длина пароля должна не менее 6 символов")
            }
        }

        //        checkFieldEnabled(et_phone, et_password, login_button);

// 		text_button_title!!.setOnClickListener { startActivity(Intent(this, HelpActivity::class.java)) }

        setFooter()
    }

    // TODO move in view model
    /**
     * Success auth user. Save token
     *
     * @param authResponse - (token,isConfirmed)
     */
    private fun onAuthResponseSuccess(authResponse: AuthResponse) {
        val confirmed = authResponse.user?.confirmed

        updateUser(authResponse)

        if (confirmed!!) {
            startActivity(Intent(this, MainActivity::class.java))
            Timber.d("Success signIn")
        } else {
            val username = authResponse.user.phone
            //TODO: Fix it!!!
//            mVm.sendSms()

            val intent = Intent(this, UserConfirmActivity::class.java)
            intent.putExtra("phone", username)
            startActivity(intent)
        }
    }

    private fun updateUser(authResponse: AuthResponse) {
        PrefUtils.name = authResponse.user?.name ?: ""
        PrefUtils.token = authResponse.token ?: ""
        PrefUtils.phone = authResponse.user?.phone ?: ""
        PrefUtils.isConfirmed = authResponse.user?.confirmed ?: false
        PrefUtils.publicid = authResponse.user?.publicId ?: ""
    }

    fun onAuthFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        if (appErrorMessage.contains("403")) longToast("Неправильный пароль")
        if (appErrorMessage.contains("404")) {
            longToast("Пользователь не найден")
        }
        if (appErrorMessage.contains("502")) longToast("Ошибка сервера")
        if (appErrorMessage.contains("No Internet Connection!"))
            longToast("Интернет подключение не доступно!")
    }

    fun onSendSmsSuccess(confirmModel: MessageResponse) {
        Timber.tag("common").d("Sms send success: %s", confirmModel.message)
        //        toast("Код выслан повторно");
    }

    fun onSmsSendFailure(appErrorMessage: String) {
        Timber.tag("common").w("Sms send error: %s", appErrorMessage)
        if (appErrorMessage.contains("404"))
            toast("Ошибка отправки кода! Нет пользователя с таким номером")
        if (appErrorMessage.contains("409")) toast("Ошибка функции кодогенерации")
        if (appErrorMessage.contains("422")) toast("Не указан номер телефона")
    }

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage("Авторизация...")
            mProgressDialog!!.isIndeterminate = true
        }

        mProgressDialog!!.show()
    }

    protected fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }

    override fun onBackPressed() {
        //        super.onBackPressed();
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        am.killBackgroundProcesses("ru.prsolution.winstrike")
        finish()
    }

    private fun setFooter() {

        val register = SpannableString("Еще нет аккаунта? Зарегистрируйтесь")
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                longToast("textview clicked")
//                startActivity(Intent(this@SignInActivity, SingUpActivity::class.java))
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_register.movementMethod = LinkMovementMethod.getInstance()
        tv_register.text = register


        val textCondAndPolicy = SpannableString("Условиями и Политикой конфиденциальности")
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                longToast("condition click")
//            val browserIntent = Intent(this, YandexWebView::class.java)
//            val url = "file:///android_asset/rules.html"
//            browserIntent.putExtra("url", url)
//            startActivity(browserIntent)
            }
        }
        val politicaClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                longToast("politica click")
//            val browserIntent = Intent(this, YandexWebView::class.java)
//            val url = "file:///android_asset/politika.html"
//            browserIntent.putExtra("url", url)
//            startActivity(browserIntent)
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(politicaClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_conditions.movementMethod = LinkMovementMethod.getInstance()
        tv_conditions.text = textCondAndPolicy


    }

}
