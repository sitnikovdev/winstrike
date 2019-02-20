package ru.prsolution.winstrike.presentation.login

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
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.LoginViewModel
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.inc_password.*
import kotlinx.android.synthetic.main.inc_phone.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.utils.*
import timber.log.Timber


/**
 * Created by Oleg Sitnikov on 2019-02-16
 */

class LoginFragment : Fragment() {

    private val mVm: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_login)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mVm.authResponse.observe(this@LoginFragment, Observer {
            it?.let {
                // TODO: process error!
                when (it.state) {
//                    ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
//                    ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
//                    ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
                }
                it.data?.let {
                    onAuthSuccess(it)
                }
                it.message?.let {
                    onAuthFailure(it)
                }
            }
        })
        initView()
    }

    private fun initView() {
        et_phone.setPhoneMask()
        login_button.isEnabled = true

        login_button.setOnClickListener {

            et_phone.validate({ et_phone.text!!.isPhoneValid() }, getString(R.string.ac_login_error_phone))

            et_password.validate(
                { et_password.text!!.isPasswordValid() },
                getString(R.string.ac_login_error_password_lengh)
            )

            when {
                et_phone.text!!.isPhoneValid() &&
                        et_password.text!!.isPasswordValid() -> {

                    val username = formatPhone(et_phone.text.toString())
                    val password = et_password.text.toString()
                    val loginModel = LoginInfo(username, password)

                    mVm.getUser(loginModel)
                }
            }
        }

        help_link_tv.setOnClickListener {
            val action = LoginFragmentDirections.nextActionHelp()
            Navigation.findNavController(requireActivity(), R.id.login_host_fragment).navigate(action)
        }

        setRegisterFooter()
        (activity as LoginActivity).setLoginPolicyFooter(tv_conditions)
    }

    private fun onAuthSuccess(authResponse: AuthResponse) {
        val confirmed = authResponse.user?.confirmed ?: false

        updateUser(authResponse)

        if (confirmed) {
            val action = LoginFragmentDirections.actionToMainActivity()
            (activity as LoginActivity).navigate(action)
        } else {
            //TODO: Fix it!!!
            longToast("Пользователь не подтвержден. Отправляем СМС и перенаправляемся на страницу подверждения СМС кода.")
            val username = authResponse.user?.phone
//            mVm.sendSms()

//            val intent = Intent(requireActivity(), CodeFragment::class.java)
//            intent.putExtra("phone", username)
//            startActivity(intent)
        }
    }

    private fun onAuthFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            appErrorMessage.contains("403") ->
                longToast(getString(ru.prsolution.winstrike.R.string.fmt_login_error_password_wrong))
            appErrorMessage.contains("502") -> longToast(getString(R.string.fmt_login_server_error))
            appErrorMessage.contains(getString(R.string.fmt_login_noinet)) ->
                longToast(getString(R.string.fmt_login_message_noinet))
        }

        fun onSendSmsSuccess(confirmModel: MessageResponse) {
            Timber.tag("common").d("Sms send success: %s", confirmModel.message)
            //        toast("Код выслан повторно");
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

    fun setRegisterFooter() {
        val register = SpannableString(getString(R.string.fmt_login_title_register))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.actionToNavigationRegister()
                Navigation.findNavController(requireActivity(), R.id.login_host_fragment).navigate(action)
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_register.movementMethod = LinkMovementMethod.getInstance()
        tv_register.text = register

    }


/*    fun onSmsSendFailure(appErrorMessage: String) {
        Timber.tag("common").w("Sms send error: %s", appErrorMessage)
        if (appErrorMessage.contains("404"))
            toast("Ошибка отправки кода! Нет пользователя с таким номером")
        if (appErrorMessage.contains("409")) toast("Ошибка функции кодогенерации")
        if (appErrorMessage.contains("422")) toast("Не указан номер телефона")
    }*/

}
