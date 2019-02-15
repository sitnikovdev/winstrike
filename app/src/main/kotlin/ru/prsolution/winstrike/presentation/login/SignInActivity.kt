package ru.prsolution.winstrike.presentation.login

import android.app.Activity
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.ac_login.et_password
import kotlinx.android.synthetic.main.ac_login.et_phone
import kotlinx.android.synthetic.main.ac_login.tv_register
import kotlinx.android.synthetic.main.ac_login.tv_register2
import kotlinx.android.synthetic.main.ac_login.tv_politica4
import kotlinx.android.synthetic.main.ac_login.tv_conditions
import kotlinx.android.synthetic.main.ac_login.v_button
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.TextFormat
import ru.prsolution.winstrike.presentation.utils.TextFormat.setTextFoot1Color
import ru.prsolution.winstrike.presentation.utils.TextFormat.setTextFoot2Color
import ru.prsolution.winstrike.presentation.utils.Utils.setBtnEnable
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.viewmodel.LoginViewModel
import timber.log.Timber

/*
 * Created by oleg on 31.01.2018.
 */
// TODO: 13/05/2018 Reorder method call in this activity.
class SignInActivity : AppCompatActivity() {

    private val mVm: LoginViewModel by viewModel()

    private var mProgressDialog: ProgressDialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_login)

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

        setBtnEnable(v_button, true)

        v_button!!.setOnClickListener {
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

        //        checkFieldEnabled(et_phone, et_password, v_button);

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

    /*
    protected void checkFieldEnabled(EditText et_phone, EditText et_pass, View button) {
        Observable<TextViewTextChangeEvent> phoneObservable = RxTextView.textChangeEvents(et_phone);
        Observable<TextViewTextChangeEvent> passwordObservable = RxTextView.textChangeEvents(et_pass);
        Observable.combineLatest(phoneObservable, passwordObservable, (phoneSelected, passwordSelected) -> {
            boolean phoneCheck = phoneSelected.text().length() >= 14;
            boolean passwordCheck = passwordSelected.text().length() >= 4;
            return phoneCheck && passwordCheck;
        }).subscribe(aBoolean -> {
            if (aBoolean) {
                setBtnEnable(button, true);
            } else {
                setBtnEnable(button, false);
            }
        });
    }
*/

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
        setTextFoot1Color(tv_register!!, "Еще нет аккаунта?", "#9b9b9b")
        setTextFoot2Color(tv_register2!!, " Зарегистрируйтесь", "#c9186c")
        tv_register2!!.setOnClickListener { startActivity(Intent(this, SingUpActivity::class.java)) }

        val textConditions = "Условиями"
        val content = SpannableString(textConditions).apply {
            setSpan(UnderlineSpan(), 0, textConditions.length, 0)
        }
        tv_conditions!!.text = content

        tv_conditions!!.setOnClickListener {
            //            val browserIntent = Intent(this, YandexWebView::class.java)
//            val url = "file:///android_asset/rules.html"
//            browserIntent.putExtra("url", url)
//            startActivity(browserIntent)
        }

        tv_politica4!!.setOnClickListener {
            //            val browserIntent = Intent(this, YandexWebView::class.java)
//            val url = "file:///android_asset/politika.html"
//            browserIntent.putExtra("url", url)
//            startActivity(browserIntent)
        }

        val textFooter = "Политикой конфиденциальности"
        val content4 = SpannableString(textFooter)
        content4.setSpan(UnderlineSpan(), 0, textFooter.length, 0)
        tv_politica4!!.text = content4
    }

}
