package ru.prsolution.winstrike.presentation.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_login.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.presentation.login.register.UserConfirmActivity
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.Constants.PASSWORD_LENGTH
import ru.prsolution.winstrike.presentation.utils.Constants.PHONE_LENGTH
import ru.prsolution.winstrike.presentation.utils.TextFormat
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.LoginViewModel
import androidx.navigation.Navigation
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.Constants.URL_CONDITION
import ru.prsolution.winstrike.presentation.utils.Constants.URL_POLITIKA


/**
 * Created by Oleg Sitnikov on 2019-02-16
 */

class LoginHomeFragment : Fragment() {

    private val mVm: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(ru.prsolution.winstrike.R.layout.fmt_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mVm.authResponse.observe(this@LoginHomeFragment, Observer {
            it?.let { response ->
                onAuthResponseSuccess(response)
            }
        })
        initView()
    }

    private fun initView() {
        TextFormat.formatText(et_phone, Constants.PHONE_MASK)

        login_button.setOnClickListener {
            when {
                et_phone?.text?.length!! >= PHONE_LENGTH
                        && et_password?.text?.length!! >= PASSWORD_LENGTH -> {

                    val username = formatPhone(et_phone.text.toString())
                    val password = et_password.text.toString()
                    val loginModel = LoginInfo(username, password)

                    mVm.getUser(loginModel)
                }
                et_phone.text.isEmpty() -> longToast(getString(ru.prsolution.winstrike.R.string.ac_login_message_phone_hint))
                et_phone.text.length < PHONE_LENGTH -> longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_phone))
                et_password.text.isEmpty() -> longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_password))
                et_password.text.length < PASSWORD_LENGTH -> longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_password_lengh))
            }
        }

        setFooter()
    }

    private fun onAuthResponseSuccess(authResponse: AuthResponse) {
        val confirmed = authResponse.user?.confirmed ?: false

        updateUser(authResponse)

        if (confirmed) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        } else {
            val username = authResponse.user?.phone
            //TODO: Fix it!!!
//            mVm.sendSms()

            val intent = Intent(requireActivity(), UserConfirmActivity::class.java)
            intent.putExtra("phone", username)
            startActivity(intent)
        }
    }

    // TODO: Use Cash (RxPaper2).
    private fun updateUser(authResponse: AuthResponse) {
        PrefUtils.name = authResponse.user?.name ?: ""
        PrefUtils.token = authResponse.token ?: ""
        PrefUtils.phone = authResponse.user?.phone ?: ""
        PrefUtils.isConfirmed = authResponse.user?.confirmed ?: false
        PrefUtils.publicid = authResponse.user?.publicId ?: ""
    }


    private fun setFooter() {
        val register = SpannableString(getString(R.string.fmt_login_title_register))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                longToast("TODO: Register user")
//                startActivity(Intent(this@SignInActivity, SingUpActivity::class.java))
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_register.movementMethod = LinkMovementMethod.getInstance()
        tv_register.text = register


        val textCondAndPolicy = SpannableString(getString(R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginHomeFragmentDirections.nextAction(URL_CONDITION)
                action.title = getString(R.string.fmt_title_condition)
                Navigation.findNavController(requireActivity(), R.id.login_host_fragment).navigate(action)

            }
        }
        val politicaClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginHomeFragmentDirections.nextAction(URL_POLITIKA)
                action.title = getString(R.string.fmt_login_title_politika)
                Navigation.findNavController(requireActivity(), R.id.login_host_fragment).navigate(action)
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(politicaClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_conditions.movementMethod = LinkMovementMethod.getInstance()
        tv_conditions.text = textCondAndPolicy
    }


/*
    fun onAuthFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        if (appErrorMessage.contains("403")) longToast("Неправильный пароль")
        if (appErrorMessage.contains("404")) {
            longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
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
*/

}