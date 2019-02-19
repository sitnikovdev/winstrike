package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fmt_register.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.login.LoginActivity
import ru.prsolution.winstrike.presentation.utils.inflate

/*
 * Created by oleg on 31.01.2018.
 */

class RegisterFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_register)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//       Next button
        register_button.setOnClickListener {
            val action = RegisterFragmentDirections.actionToNavigationCode()
            (activity as LoginActivity).navigate(action)
        }
        (activity as LoginActivity).setLoginFooter(tv_register)
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

