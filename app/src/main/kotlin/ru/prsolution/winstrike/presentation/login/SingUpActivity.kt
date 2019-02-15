package ru.prsolution.winstrike.presentation.login

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.ac_registration.et_password
import kotlinx.android.synthetic.main.ac_registration.et_phone
import kotlinx.android.synthetic.main.ac_registration.next_button_phone
import kotlinx.android.synthetic.main.ac_registration.tv_register
import kotlinx.android.synthetic.main.ac_registration.tv_register2
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.TextFormat
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.UserModel
import timber.log.Timber
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.presentation.utils.TextFormat.setTextFoot1Color
import ru.prsolution.winstrike.presentation.utils.TextFormat.setTextFoot2Color
import ru.prsolution.winstrike.datasource.model.login.AuthResponseEntity
import ru.prsolution.winstrike.domain.models.login.SmsModel
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.phone
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.token

/*
 * Created by oleg on 31.01.2018.
 */

class SingUpActivity : AppCompatActivity() {

    private var presenter: RegisterPresenter? = null
    private var user: LoginModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        renderView()
        init()
        presenter = RegisterPresenter()
    }

    fun init() {
        next_button_phone!!.setOnClickListener {
            // Создание пользователя и переход на страницу подтверждения пароля
            user = LoginModel(
                phone = formatPhone(et_phone?.text.toString()),
                password = et_password?.text.toString()
            )

            presenter!!.createUser(user!!)
        }

        TextFormat.formatText(et_phone, Constants.PHONE_MASK)

        setFooter()
    }

    fun onSendSmsSuccess(authResponse: MessageResponse) {
        Timber.d("Sms send successfully: %s", authResponse.message)
        toast("Код выслан")
        val intent = Intent(this@SingUpActivity, UserConfirmActivity::class.java)
        intent.putExtra("phone", user!!.phone)
        startActivity(intent)
    }

    fun onSmsSendFailure(appErrorMessage: String) {
        Timber.d("Sms send failure: %s", appErrorMessage)
    }

    private fun getConfirmSmsModel(phone: String): SmsModel {
        return SmsModel(phone)
    }

    private fun setFooter() {
        setTextFoot1Color(tv_register!!, "Уже есть аккаунт?", "#9b9b9b")
        setTextFoot2Color(tv_register2!!, "Войдите", "#c9186c")

        tv_register2!!.setOnClickListener { startActivity(Intent(this, SignInActivity::class.java)) }
    }

    fun renderView() {
        setContentView(R.layout.ac_registration)
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

        val intent = Intent(this@SingUpActivity, UserConfirmActivity::class.java)
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

    override fun onStop() {
        super.onStop()
        presenter!!.onStop()
    }

    /*    protected void checkFieldEnabled(EditText et_phone, EditText et_pass, View button) {
        Observable<TextViewTextChangeEvent> phoneObservable = RxTextView.textChangeEvents(et_phone);
        Observable<TextViewTextChangeEvent> et_passwordObservable = RxTextView.textChangeEvents(et_pass);
        Observable.combineLatest(phoneObservable, et_passwordObservable, (phoneSelected, et_passwordSelected) -> {
            boolean phoneCheck = phoneSelected.text().length() >= 14;
            boolean et_passwordCheck = et_passwordSelected.text().length() >= 4;
            return phoneCheck && et_passwordCheck;
        }).subscribe(aBoolean -> {
            if (aBoolean) {
                setBtnEnable(button, true);
            } else {
                setBtnEnable(button, false);
            }
        });
    }*/
}
