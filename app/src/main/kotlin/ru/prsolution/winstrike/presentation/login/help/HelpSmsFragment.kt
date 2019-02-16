package ru.prsolution.winstrike.presentation.login.help

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.ac_smshelp.et_code
import kotlinx.android.synthetic.main.ac_smshelp.et_phone
import kotlinx.android.synthetic.main.ac_smshelp.next_button_confirm
import kotlinx.android.synthetic.main.ac_smshelp.next_button_phone
import kotlinx.android.synthetic.main.ac_smshelp.tv_register
import kotlinx.android.synthetic.main.ac_smshelp.tv_register2
import org.jetbrains.anko.support.v4.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.utils.TextFormat
import ru.prsolution.winstrike.domain.models.common.TimerViewModel
import timber.log.Timber
import ru.prsolution.winstrike.presentation.utils.TextFormat.setTextFoot1Color
import ru.prsolution.winstrike.presentation.utils.TextFormat.setTextFoot2Color
import ru.prsolution.winstrike.domain.models.login.PasswordModel
import ru.prsolution.winstrike.domain.models.login.SmsModel
import ru.prsolution.winstrike.presentation.login.LoginActivity
import ru.prsolution.winstrike.presentation.utils.Utils.setBtnEnable

/**
 * Created by oleg on 15/03/2018.
 */

class HelpSmsFragment : Fragment() {
    private var dialog: Dialog? = null

    private var timer: TimerViewModel? = null

    private val confirmSmsModel: SmsModel
        get() {
            val phone = TextFormat.formatPhone(et_phone!!.text.toString())
            return SmsModel(phone)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ac_smshelp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Высылаем код
        next_button_phone!!.setOnClickListener { view ->
            if (!TextUtils.isEmpty(et_phone!!.text) && et_phone!!.text.length >= 14) {
                // Запрос кода подтверждения повторно
                val auth = confirmSmsModel
                sendSms(auth)
                timer!!.startButtonClicked()
                setBtnEnable(next_button_phone, false)
            } else {
                toast("Введите номер телефона!")
            }
        }

        /*
         * Пользователь вводит код и нажимает кнопку "Подтвердить"
         *
         */

        next_button_confirm!!.setOnClickListener { it ->
            if (!TextUtils.isEmpty(et_code!!.text) && et_code!!.text.length >= 4) {
                val phone = TextFormat.formatPhone(et_phone!!.text.toString())
                val smsCode = et_code!!.text.toString()
                // TODO make model immutable
                // Показываем диалог для смены пароля:
                dlgRefreshPassword(PasswordModel(phone, ""), smsCode)
            } else {
                toast("Введите код подтверждения!")
            }
        }

        TextFormat.formatText(et_phone, "(___) ___-__-__")

        // Меняем видимость кнопки вводе кода

        /*        RxTextView.textChanges(et_code).subscribe(
                it -> {
                    Boolean fieldOk = et_code.getText().length() >= 6;
                    Boolean phoneOk = et_phone.getText().length() == 15;
                    if (fieldOk && phoneOk) {
                        setBtnEnable(next_button_confirm, true);
                    } else {
                        setBtnEnable(next_button_confirm, false);
                    }
                }
        );*/
        /*        RxTextView.textChanges(et_phone).subscribe(
                it -> {
                    Boolean phoneOk = et_phone.getText().length() == 15;
                    if (phoneOk) {
                        setBtnEnable(next_button_phone, true);
                    } else {
                        setBtnEnable(next_button_phone, false);
                    }
                }
        );*/

        setTextFoot1Color(tv_register!!, "Уже есть аккаунт?", "#9b9b9b")
        setTextFoot2Color(tv_register2!!, "Войдите", "#c9186c")

//        tv_register2!!.setOnClickListener { it -> startActivity(Intent(this@HelpSmsFragment, SignInActivity::class.java)) }

    }


    private fun dlgSendSMS() {
        dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dlg_send_sms)

        val cvBtnOk = dialog!!.findViewById<TextView>(R.id.btn_ok)

        cvBtnOk.setOnClickListener { it -> dialog!!.dismiss() }

        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val window = dialog!!.window
        val wlp = window!!.attributes

        wlp.dimAmount = 0.0f

        wlp.gravity = Gravity.CENTER
        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog!!.window!!.setDimAmount(0.5f)
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //        wlp.y = 200;
        window.attributes = wlp

        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog!!.show()
    }

    private fun dlgRefreshPassword(passw: PasswordModel, smsCode: String) {
        val phone = passw.username
        dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dlg_refresh_passw)

        val cvBtnOk = dialog!!.findViewById<View>(R.id.v_button)
        val passOne = dialog!!.findViewById<EditText>(R.id.et_password_first)
        val passTwo = dialog!!.findViewById<EditText>(R.id.et_password_second)

        cvBtnOk.setOnClickListener { it ->
            val passTexOne = passOne.text.toString()
            val passTexTwo = passTwo.text.toString()
            // check for password correctness
            if (!TextUtils.isEmpty(passTexOne) && !TextUtils.isEmpty(passTexTwo)) {
                if (passOne.text.length < 6 || passTwo.text.length < 6) {
                    toast("Длина пароля должна быть не менее 6 символов")
                }
                if (passTexOne == passTexTwo) {

                    refreshPassword(PasswordModel(phone, passTexOne), smsCode)
                    dialog!!.dismiss()
                } else {
                    toast("Пароли не совпадают")
                }
            } else {
                toast("Длина пароля должна быть не менее 6 символов")
            }
        }

        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val window = dialog!!.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog!!.window!!.setDimAmount(0.5f)
        window.attributes = wlp

        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog!!.show()
    }

    fun sendSms(smsModel: SmsModel) {

/*		val subscription = service!!.sendSmsByUserRequest(object : Service.SmsCallback {
			override fun onSuccess(authResponse: MessageResponse) {
				onSendSmsSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				onAuthFailure(networkError.appErrorMessage)
			}

		}, smsModel)

		subscriptions!!.add(subscription)*/
    }

    private fun onAuthFailure(appErrorMessage: String) {
        when (appErrorMessage) {
            "409" -> {
                Timber.tag("OkHttp").d("Не верный код")
                toast("Не верный код")
            }
            "403" -> {
                Timber.tag("OkHttp").d("Пользователь уже поддвержден")
                toast("Пользователь уже поддвержден")
                setBtnEnable(next_button_phone, false)
            }
            "400" -> {
                Timber.tag("OkHttp").d("Пользователь не найден")
                toast("Пользователь не зарегистрирован")
            }
            "404" -> {
                Timber.tag("OkHttp").d("Пользователь уже поддвержден")
                toast("Пользователь уже поддвержден")
            }
            else -> {
                Timber.tag("OkHttp").d("confirm code: %s", appErrorMessage)
                toast("Пользователь не подтвержден")
            }
        }
    }

    private fun onSendSmsSuccess(authResponse: MessageResponse) {
        Timber.d("SMS код выслан")
        // Show dialog, that sms succesfully send
        dlgSendSMS()
//        setConfirmVisible(true)
    }

    fun refreshPassword(smsModel: PasswordModel, smsCode: String) {
        //        getViewState().showWait();

/*		val subscription = service!!.refreshPassword(object : Service.RefressPasswordCallback {
			override fun onSuccess(authResponse: MessageResponse) {
				onRefreshPasswordSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				onRefressPasswordFailure(networkError.appErrorMessage)
			}

		}, smsModel, smsCode)

		subscriptions!!.add(subscription)*/
    }

    private fun onRefressPasswordFailure(appErrorMessage: String) {
        when (appErrorMessage) {
            "409" -> {
                Timber.tag("OkHttp").d("Не верный код")
                toast("Не верный код")
            }
            "403" -> {
                Timber.tag("OkHttp").d("Пользователь уже поддвержден")
                toast("Пользователь уже поддвержден")
                setBtnEnable(next_button_phone, false)
            }
            "400" -> {
                Timber.tag("OkHttp").d("Пользователь не найден")
                toast("Пользователь с таким номером не найден")
            }
            "404" -> {
                Timber.tag("OkHttp").d("Пользователь уже поддвержден")
                toast("Пользователь уже поддвержден")
            }
            else -> {
                Timber.tag("OkHttp").d("confirm code: %s", appErrorMessage)
                toast("Пользователь не подтвержден")
            }
        }
    }

    private fun onRefreshPasswordSuccess(authResponse: MessageResponse) {
        toast("Новый пароль успешно сохранен")
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
    }

    override fun onStop() {
        super.onStop()
    }
}
