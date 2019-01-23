package ru.prsolution.winstrike.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.ac_confsmscode.confirm_button
import kotlinx.android.synthetic.main.ac_confsmscode.displayWorkTimeLeft
import kotlinx.android.synthetic.main.ac_confsmscode.et_code
import kotlinx.android.synthetic.main.ac_confsmscode.et_name
import kotlinx.android.synthetic.main.ac_confsmscode.tv_register2
import kotlinx.android.synthetic.main.ac_confsmscode.tv_register4
import kotlinx.android.synthetic.main.ac_confsmscode.tv_confirm_btn
import kotlinx.android.synthetic.main.ac_confsmscode.tv_hint
import kotlinx.android.synthetic.main.ac_confsmscode.tv_nextbtn_label
import kotlinx.android.synthetic.main.ac_confsmscode.tv_send_code_again
import kotlinx.android.synthetic.main.ac_confsmscode.tv_send_code_again_timer
import kotlinx.android.synthetic.main.ac_confsmscode.v_name
import kotlinx.android.synthetic.main.ac_confsmscode.v_nextbtn
import kotlinx.android.synthetic.main.ac_confsmscode.v_send_code_again
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.presentation.utils.webview.YandexWebView
import ru.prsolution.winstrike.presentation.utils.pref.AuthUtils
import ru.prsolution.winstrike.domain.models.ConfirmModel
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.domain.models.ProfileModel
import ru.prsolution.winstrike.domain.models.TimerViewModel
import ru.prsolution.winstrike.domain.models.UserEntity
import ru.prsolution.winstrike.networking.Service
import timber.log.Timber

import ru.prsolution.winstrike.common.utils.TextFormat.setTextColor
import ru.prsolution.winstrike.common.utils.TextFormat.simplePhoneFormat
import ru.prsolution.winstrike.common.utils.Utils.setBtnEnable
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel

/**
 * Created by oleg on 15/03/2018.
 */

class UserConfirmActivity : AppCompatActivity(), TimerViewModel.TimeFinishListener {


	var service: Service? = null

	private var presenter: UserConfirmPresenter? = null
	private var user: ConfirmModel? = null
	private var phone: String? = null
	private var timer: TimerViewModel? = null

	override fun onTimeFinish() {
		//        setBtnEnable(send_code_again,true);
		timer!!.stopButtonClicked()

		runOnUiThread {
			displayWorkTimeLeft!!.visibility = View.INVISIBLE

			v_send_code_again!!.visibility = View.VISIBLE
			tv_send_code_again!!.visibility = View.VISIBLE
		}
	}

	override fun onStop() {
		super.onStop()
		presenter!!.onStop()
	}


	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter = UserConfirmPresenter(service)


		renderView()
		init()

	}

	private fun renderView() {
		//        setContentView(R.layout.ac_confsmscode);
		timer = TimerViewModel()
		timer!!.listener = this

		setContentView(R.layout.ac_confsmscode)
	}

	private fun init() {

		phone = intent.getStringExtra("phone")
		if (phone == null) {
			phone = "9520757099"
			AuthUtils.phone = phone as String
		}

		confirmFalse()

		setBtnEnable(v_nextbtn, false)

		v_send_code_again!!.setOnClickListener { it ->
			val smsModel = ConfirmSmsModel()
			smsModel.username = AuthUtils.phone
			presenter!!.sendSms(smsModel)
		}

		tv_send_code_again!!.visibility = View.INVISIBLE
		v_send_code_again!!.visibility = View.INVISIBLE

		displayWorkTimeLeft!!.visibility = View.VISIBLE
		timer!!.startButtonClicked()


		// Меняем видимость кнопки  корректном вводе кода из смс
		/*
        RxTextView.textChanges(et_code).subscribe(
                it -> {
                    Boolean fieldOk = et_code.getText().length() >= 6;
                    if (fieldOk) {
                        setBtnEnable(confirm_button, true);
                    } else {
                        setBtnEnable(confirm_button, false);
                    }
                }
        );
*/

		// Подтверждаем пользователя (отправляем серверу запрос с кодом введеным пользователем)
		confirm_button!!.setOnClickListener { it ->
			val sms_code = et_code!!.text.toString()
			Timber.d("sms_code: %s", sms_code)
			tv_send_code_again!!.visibility = View.GONE
			v_send_code_again!!.visibility = View.GONE

			displayWorkTimeLeft!!.visibility = View.GONE
			timer!!.stopButtonClicked()

			/*                    if (dpHeight < 600) {
                        et_codeBackGround.setVisibility(View.GONE);
                        et_code.setVisibility(View.GONE);
                    }*/

			// Hide keyboard
			val view = this.currentFocus
			if (view != null) {
				val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
				imm.hideSoftInputFromWindow(view.windowToken, 0)
			}

			user = ConfirmModel()
			user!!.phone = phone
			presenter!!.confirmUser(sms_code, user!!)
		}

		// Вводим имя пользователя и переходим на главный экран
		/*        RxTextView.textChanges(et_name).subscribe(
                it -> {
                }
        );*/

		et_name!!.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

			}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				val fieldOk = et_name!!.text.length >= 4
				if (fieldOk) {
					// Update user profile - set name.
					val publicId = AuthUtils.publicid
					val token = "Bearer " + AuthUtils.token
					val profile = ProfileModel()
					profile.name = et_name!!.text.toString()
					setBtnEnable(v_nextbtn, true)
					tv_nextbtn_label!!.text = "Поехали!"
					v_nextbtn!!.setOnClickListener { v ->

						val userDb = UserEntity()
						userDb.setConfirmed(true)
						userDb.setPhone(user!!.phone.toString())
						AuthUtils.publicid?.let { userDb.setPublickId(it) }
						userDb.setToken(AuthUtils.token)
						userDb.setName(profile.name!!)
						AuthUtils.name = userDb.getName()!!

						//                                    repository.insertUser(userDb);

						publicId?.let { presenter!!.updateProfile(token, profile, it) }
						startActivity(Intent(this@UserConfirmActivity, SignInActivity::class.java))
					}
				} else {
					setBtnEnable(confirm_button, false)
					setBtnEnable(v_nextbtn, false)
				}

			}

			override fun afterTextChanged(s: Editable) {

			}
		})


		setTextColor(tv_hint!!, "Введите 6-значный код, который был\n" + "отправлен на номер",
		             simplePhoneFormat(phone!!), "#9b9b9b", "#000000")

		setFooter()

	}

	fun onUserConfirmSuccess(confirmModel: MessageResponse) {
		Timber.d("UserEntity confirm successfully: %s", confirmModel.message)
		//        toast("Пользователь подтвержден");
		//        setBtnEnable(confirm_button, false);
		// Restore token if user in logout state now

		confirmSuccess()
	}

	fun onUserConfirmFailure(appErrorMessage: String) {
		Timber.w("UserEntity confirm failure: %s", appErrorMessage)
		if (appErrorMessage.contains("409")) toast("Не верный код")
		if (appErrorMessage.contains("403")) toast("Пользователь уже поддвержден")
		if (appErrorMessage.contains("404"))
			toast("Ошибка регистрации! Возможно код неверен или пользователь уже существует")
		if (appErrorMessage.contains("406")) toast("Код просрочен")
		//        confirmFalse();
		// TODO: 22/05/2018 Changed for test:
		//        confirmSuccess();
		confirmFalse()
	}


	fun onSendSmsSuccess(authResponse: MessageResponse) {
		Timber.d("Sms send successfully: %s", authResponse.message)
		toast("Код выслан")

		//        displayWorkTimeLeft.setVisibility(View.VISIBLE);

	}

	fun onSmsSendFailure(appErrorMessage: String) {
		Timber.d("Sms send failure: %s", appErrorMessage)
	}


	protected fun toast(message: String) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
	}

	private fun confirmSuccess() {
		confirm_button!!.visibility = View.GONE
		tv_confirm_btn!!.visibility = View.INVISIBLE

		et_name!!.visibility = View.VISIBLE
		v_name!!.visibility = View.VISIBLE
		v_nextbtn!!.visibility = View.VISIBLE
		tv_nextbtn_label!!.visibility = View.VISIBLE

		v_send_code_again!!.visibility = View.INVISIBLE
		tv_send_code_again_timer!!.visibility = View.INVISIBLE
	}

	private fun confirmFalse() {
		confirm_button!!.visibility = View.VISIBLE
		tv_confirm_btn!!.visibility = View.VISIBLE

		et_name!!.visibility = View.INVISIBLE
		v_name!!.visibility = View.INVISIBLE

		v_nextbtn!!.visibility = View.INVISIBLE
		tv_nextbtn_label!!.visibility = View.INVISIBLE

		v_send_code_again!!.visibility = View.VISIBLE
		tv_send_code_again_timer!!.visibility = View.VISIBLE
		tv_send_code_again_timer!!.visibility = View.INVISIBLE

	}

	private fun setFooter() {
		val mystring = "Условиями"
		val content = SpannableString(mystring)
		content.setSpan(UnderlineSpan(), 0, mystring.length, 0)
		tv_register2!!.text = content


		tv_register2!!.setOnClickListener { it ->
			val browserIntent = Intent(this, YandexWebView::class.java)
			val url = "file:///android_asset/rules.html"
			browserIntent.putExtra("url", url)
			startActivity(browserIntent)
		}

		tv_register4!!.setOnClickListener { it ->
			val browserIntent = Intent(this, YandexWebView::class.java)
			//                    String url = "file:///android_asset/politika.html";
			val url = "https://winstrike.gg/WinstrikePrivacyPolicy.pdf"
			browserIntent.putExtra("url", url)
			startActivity(browserIntent)
		}

		val textFooter = "Политикой конфиденциальности"
		val content4 = SpannableString(textFooter)
		content4.setSpan(UnderlineSpan(), 0, textFooter.length, 0)
		tv_register4!!.text = content4
	}

	fun showWait() {}

	fun removeWait() {}

	fun onProfileUpdateSuccessfully(authResponse: MessageResponse) {
		Timber.d("Profile is updated")
		//        Toast.makeText(this, "Профиль успешно обновлен", Toast.LENGTH_LONG).show();
	}

	fun onFailtureUpdateProfile(appErrorMessage: String) {
		Timber.d("Wrong update profile")
		Toast.makeText(this, "Не удалось обновить профиль", Toast.LENGTH_LONG).show()
	}
}
