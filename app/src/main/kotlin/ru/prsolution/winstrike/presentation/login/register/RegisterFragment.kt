package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fmt_login.*
import kotlinx.android.synthetic.main.fmt_register.*
import kotlinx.android.synthetic.main.inc_password.*
import kotlinx.android.synthetic.main.inc_phone.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.login.LoginActivity
import ru.prsolution.winstrike.presentation.login.LoginFragmentDirections
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.utils.*
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone

/*
 * Created by oleg on 31.01.2018.
 */

class RegisterFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_register)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//       Next button - navigate to Code
        register_button.setOnClickListener {
            val action = RegisterFragmentDirections.actionToNavigationCode()
            action.phone = "+79520757099"
            (activity as LoginActivity).navigate(action)
        }
//        initView()
        (activity as LoginActivity).setRegisterLoginFooter(tv_register_footer)
    }

    private fun initView() {
        // Validate phone and password and register user
        et_phone.setPhoneMask()

        register_button.setOnClickListener {

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

//                    mVm.getUser(loginModel)

                    val action = RegisterFragmentDirections.actionToNavigationCode()
                    (activity as LoginActivity).navigate(action)
                }
            }
        }

    }



    fun init() {
/*        next_button_phone!!.setOnClickListener {
            // Создание пользователя и переход на страницу подтверждения пароля
            user = LoginModel(
                phone = formatPhone(et_phone?.text.toString()),
                password = et_password?.text.toString()
            )

//            presenter!!.createUser(user!!)
        }

    }

    fun onSendSmsSuccess(authResponse: MessageResponse) {
        Timber.d("Sms send successfully: %s", authResponse.message)
        toast("Код выслан")
        val intent = Intent(requireActivity(), CodeFragment::class.java)
        intent.putExtra("phone", user!!.phone)
        startActivity(intent)
    }

    fun onSmsSendFailure(appErrorMessage: String) {
        Timber.d("Sms send failure: %s", appErrorMessage)
    }

    private fun getConfirmSmsModel(phone: String): SmsModel {
        return SmsModel(phone)
    }


    /**
     * Register new user and send him sms with confirm code.
     */
    fun onRegisterSuccess(authResponse: AuthResponseEntity) {
        with(PrefUtils) {
            token = authResponse.token
            publicid = authResponse.user?.publicId!!
            isConfirmed = false
            phone = user?.phone
            name = "NoName"
        }

        val userDb = UserModel(
            id = null,
            confirmed = false,
            phone = user?.phone,
            publickId = authResponse.user?.publicId,
            token = authResponse.token
        )

        val intent = Intent(requireActivity(), CodeFragment::class.java)
        intent.putExtra("phone", user!!.phone)
        startActivity(intent)
    }

    fun onRegisterFailure(appErrorMessage: String) {
        Timber.d("Register failure: %s", appErrorMessage)
        if (appErrorMessage.contains("409")) longToast("Пользователь уже существует")
        if (appErrorMessage.contains("422")) longToast("Пароль слишком короткий.")
        if (appErrorMessage.contains("413")) {
            Timber.w("RegisterUser: Передан не правильный формат данных JSON")
            longToast("Пользователь не создан")
        }
    }

*/

    }


}

