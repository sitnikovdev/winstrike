package ru.prsolution.winstrike.presentation.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.ac_login.*
import kotlinx.android.synthetic.main.fmt_login.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.presentation.login.register.UserConfirmActivity
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.TextFormat
import ru.prsolution.winstrike.presentation.utils.Utils.setBtnEnable
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.LoginViewModel
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-02-16
 */

class LoginHomeFragment : Fragment() {

    private val mVm: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fmt_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mVm.getUser()
        }

        mVm.authResponse.observe(this@LoginHomeFragment, Observer {
            it.let {
                it?.let { response ->
                    onAuthResponseSuccess(response)
                }
            }
        })
        initView()
    }

    fun initView() {
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
                longToast(getString(R.string.ac_login_message_phone_hint))
            } else if (TextUtils.isEmpty(et_password!!.text)) {
                longToast(getString(R.string.ac_login_error_password))
            } else if (et_phone!!.text.length < Constants.PHONE_LENGTH) {
                longToast(getString(R.string.ac_login_error_phone))
            } else if (et_password!!.text.length < Constants.PASSWORD_LENGTH) {
                longToast(getString(R.string.ac_login_error_password_lengh))
            }
        }

        //        checkFieldEnabled(et_phone, et_password, login_button);

// 		text_button_title!!.setOnClickListener { startActivity(Intent(this, HelpActivity::class.java)) }

        setFooter()
    }

    private fun onAuthResponseSuccess(authResponse: AuthResponse) {
        val confirmed = authResponse.user?.confirmed

        updateUser(authResponse)

        if (confirmed!!) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            Timber.d("Success signIn")
        } else {
            val username = authResponse.user.phone
            //TODO: Fix it!!!
//            mVm.sendSms()

            val intent = Intent(requireActivity(), UserConfirmActivity::class.java)
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
            longToast(getString(R.string.ac_login_error_user_not_found))
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