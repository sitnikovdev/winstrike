package ru.prsolution.winstrike.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.ac_registration.et_password
import kotlinx.android.synthetic.main.ac_registration.et_phone
import kotlinx.android.synthetic.main.ac_registration.next_button_phone
import kotlinx.android.synthetic.main.ac_registration.text_footer
import kotlinx.android.synthetic.main.ac_registration.text_footer2
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.common.utils.AuthUtils
import ru.prsolution.winstrike.common.utils.TextFormat
import ru.prsolution.winstrike.domain.models.LoginModel
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.domain.models.UserEntity
import ru.prsolution.winstrike.networking.Service
import timber.log.Timber

import ru.prsolution.winstrike.common.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot1Color
import ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot2Color
import ru.prsolution.winstrike.datasource.model.AuthResponse
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel

/*
 * Created by oleg on 31.01.2018.
 */

class SingUpActivity : AppCompatActivity(){


	var service: Service? = null

	private var presenter: RegisterPresenter? = null
	private var user: LoginModel? = null

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		WinstrikeApp.instance.appComponent?.inject(this)

		renderView()

		init()

		presenter = RegisterPresenter(service)

	}

	internal fun init() {
		//        setBtnEnable(next_button_phone, false);

		next_button_phone!!.setOnClickListener { view ->
			// Создание пользователя и переход на страницу подтверждения пароля
			user = LoginModel()
			user!!.phone = formatPhone(et_phone!!.text.toString())
			user!!.password = et_password!!.text.toString()
			Timber.tag("common").d("Create new user...")

			presenter!!.createUser(user!!)

			// TODO: 22/05/2018 For test (Send sms already confirmed user):
			/*                    ConfirmSmsModel auth = getConfirmSmsModel(user.getPhone());
                    presenter.sendSms(auth);*/
		}

		//        checkFieldEnabled(et_phone, et_password, next_button_phone);

		TextFormat.formatText(et_phone, "(___) ___-__-__")

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


	private fun getConfirmSmsModel(phone: String): ConfirmSmsModel {
		val auth = ConfirmSmsModel()
		auth.username = phone
		return auth
	}


	private fun setFooter() {
		setTextFoot1Color(text_footer!!, "Уже есть аккаунт?", "#9b9b9b")
		setTextFoot2Color(text_footer2!!, "Войдите", "#c9186c")

		text_footer2!!.setOnClickListener { it -> startActivity(Intent(this, SignInActivity::class.java)) }
	}


	internal fun renderView() {
		setContentView(R.layout.ac_registration)
	}


	 fun showWait() {}

	 fun removeWait() {}


	/**
	 * Register new user and send him sms with confirm code.
	 */
	 fun onRegisterSuccess(authResponse: AuthResponse) {
		Timber.d("Register success: %s", authResponse)
		//        toast("Пользователь создан");
		//        setOperation();
		//        setConfirmed(false);

		//saveUser(authResponse);
		AuthUtils.token = authResponse.token.toString()
		AuthUtils.publicid = authResponse.user?.publicId!!
		AuthUtils.isConfirmed = false
		AuthUtils.phone = user!!.phone.toString()
		AuthUtils.name = "NoName"

		val userDb = UserEntity()
		userDb.setConfirmed(false)
		userDb.setPhone(user!!.phone.toString())
		userDb.setPublickId(authResponse.user!!.publicId!!)
		userDb.setToken(authResponse.token)
		userDb.setName("NoName")


		Timber.d("Sms send successfully: %s", authResponse.message)
		val intent = Intent(this@SingUpActivity, UserConfirmActivity::class.java)
		intent.putExtra("phone", user!!.phone)
		startActivity(intent)
	}

	 fun onRegisterFailure(appErrorMessage: String) {
		Timber.d("Register failure: %s", appErrorMessage)
		if (appErrorMessage.contains("409")) toast("Пользователь уже существует")
		if (appErrorMessage.contains("422")) toast("Пароль слишком короткий.")
		if (appErrorMessage.contains("413")) {
			Timber.w("RegisterUser: Передан не правильный формат данных JSON")
			toast("Пользователь не создан")
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

	protected fun toast(message: String) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
	}
}
