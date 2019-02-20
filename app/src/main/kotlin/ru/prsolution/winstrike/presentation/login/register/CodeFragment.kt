package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_code.*
import kotlinx.android.synthetic.main.fmt_register.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.login.LoginActivity
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.utils.*
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.SmsViewModel
import timber.log.Timber

/**
 * Created by oleg on 15/03/2018.
 */

class CodeFragment : Fragment() {

    private val mSmsVm: SmsViewModel by viewModel()

    //    private var presenter: UserConfirmPresenter? = null
    private var mPhone: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_code)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mSmsVm.messageResponse.observe(this@CodeFragment, Observer {
            it?.let {
                // TODO: process error!
                when (it.state) {
//                    ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
//                    ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
//                    ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
                }
                it.data?.let {
                    onConfirmSuccess(it)
                }
                it.message?.let {
                    onConfirmFailure(it)
                }
            }
        })


        // Show phone in hint
        arguments?.let {
            val safeArgs = CodeFragmentArgs.fromBundle(it)
            this.mPhone = safeArgs.phone
        }

        // Next button
/*
        next_button.setOnClickListener {
            val action = CodeFragmentDirections.actionToNameFragment()
            action.phone = mPhone
            (activity as LoginActivity).navigate(action)
        }
*/

        initView()
        (activity as LoginActivity).setPhoneHint(hint_tv, mPhone)
        (activity as LoginActivity).setCodePolicyFooter(tv_policy)
    }


    private fun onConfirmSuccess(message: MessageResponse) {
        PrefUtils.isConfirmed = true
        val action = CodeFragmentDirections.actionToNameFragment()
        action.phone = mPhone
        (activity as LoginActivity).navigate(action)
    }


    private fun onConfirmFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }


    private fun initView() {
//       Next button -  confirm User and navigate to Name
        next_button.setOnClickListener {

            et_code.validate({ et_code.text!!.isCodeValid() }, getString(R.string.fmt_code_error_lengh))

            when {
                et_code.text!!.isCodeValid() -> {
                    val code = et_code.text.toString()
                    val smsInfo = SmsInfo(mPhone)

                    mSmsVm.confirm(code, smsInfo)

                }
            }
        }

    }


    private fun init() {
/*        phone = intent.getStringExtra("phone")
        if (phone == null) {
            phone = "9520757099"
            PrefUtils.phone = phone as String
        }*/

//        confirmFalse()

//        setBtnEnable(v_nextbtn, false)

//        v_send_code_again!!.setOnClickListener { it ->
//            presenter!!.sendSms(SmsModel(PrefUtils.phone))
//        }

//        tv_send_code_again!!.visibility = View.INVISIBLE
//        v_send_code_again!!.visibility = View.INVISIBLE

//        displayWorkTimeLeft!!.visibility = View.VISIBLE
//        timer!!.startButtonClicked()

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
/*        confirm_button!!.setOnClickListener { it ->
            val sms_code = et_code!!.text.toString()
            Timber.d("sms_code: %s", sms_code)
            tv_send_code_again!!.visibility = View.GONE
            v_send_code_again!!.visibility = View.GONE

            displayWorkTimeLeft!!.visibility = View.GONE
            timer!!.stopButtonClicked()

            *//*                    if (dpHeight < 600) {
                        et_codeBackGround.setVisibility(View.GONE);
                        et_code.setVisibility(View.GONE);
                    }*//*

            // Hide keyboard
//            val view = this.currentFocus
            if (view != null) {
//                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            user = SmsModel(phone)
//            presenter!!.confirmUser(sms_code, user!!)*/

/*        et_name!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val fieldOk = et_name!!.text.length >= 4
                if (fieldOk) {
                    // Update user profile - set name.
                    val publicId = PrefUtils.publicid
                    val token = "Bearer " + PrefUtils.token

                    val profile = ProfileModel(
                        name = et_name!!.text.toString()
                    )
//                    publicId?.let { presenter!!.updateProfile(token, profile, it) }

                    setBtnEnable(v_nextbtn, true)

                    tv_nextbtn_label!!.text = "Поехали!"
                    v_nextbtn!!.setOnClickListener {
                        // Save user in db (may be remove it?)
                        *//*						with(AuthUtils) {
													val userDb = UserEntity(
															confirmed = true,
															phone = user?.phone,
															publickId = publicid,
															token = token,
															name = profile.name
													)
													AuthUtils.name = userDb.name
												}*//*

//                        startActivity(Intent(this@CodeFragment, LoginActivity::class.java))
                    }
                } else {
                    setBtnEnable(confirm_button, false)
                    setBtnEnable(v_nextbtn, false)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })*/

/*        setTextColor(
            tv_hint!!, "Введите 6-значный код, который был\n" + "отправлен на номер",
            simplePhoneFormat(phone!!), "#9b9b9b", "#000000"
        )

        setRegisterLoginFooter()*/
    }

    fun onUserConfirmSuccess(confirmModel: MessageResponse) {
//        Timber.d("UserEntity confirm successfully: %s", confirmModel.message)
        //        toast("Пользователь подтвержден");
        //        setBtnEnable(confirm_button, false);
        // Restore token if user in logout state now

//        confirmSuccess()
    }

    fun onUserConfirmFailure(appErrorMessage: String) {
//        Timber.w("UserEntity confirm failure: %s", appErrorMessage)
//        if (appErrorMessage.contains("409")) toast("Не верный код")
//        if (appErrorMessage.contains("403")) toast("Пользователь уже поддвержден")
//        if (appErrorMessage.contains("404"))
//            toast("Ошибка регистрации! Возможно код неверен или пользователь уже существует")
//        if (appErrorMessage.contains("406")) toast("Код просрочен")
        //        confirmFalse();
        // TODO: 22/05/2018 Changed for test:
        //        confirmSuccess();
//            confirmFalse()
    }


    private fun confirmSuccess() {
/*        confirm_button!!.visibility = View.GONE
        tv_confirm_btn!!.visibility = View.INVISIBLE

        et_name!!.visibility = View.VISIBLE
        v_name!!.visibility = View.VISIBLE
        v_nextbtn!!.visibility = View.VISIBLE
        tv_nextbtn_label!!.visibility = View.VISIBLE

        v_send_code_again!!.visibility = View.INVISIBLE
        tv_send_code_again_timer!!.visibility = View.INVISIBLE*/
    }

    private fun confirmFalse() {
/*        confirm_button!!.visibility = View.VISIBLE
        tv_confirm_btn!!.visibility = View.VISIBLE

        et_name!!.visibility = View.INVISIBLE
        v_name!!.visibility = View.INVISIBLE

        v_nextbtn!!.visibility = View.INVISIBLE
        tv_nextbtn_label!!.visibility = View.INVISIBLE

        v_send_code_again!!.visibility = View.VISIBLE
        tv_send_code_again_timer!!.visibility = View.VISIBLE
        tv_send_code_again_timer!!.visibility = View.INVISIBLE*/
    }

    // TODO Copy paste code - remove it
    private fun setLoginFooter() {
//        val mystring = "Условиями"
//        val content = SpannableString(mystring)
//        content.setSpan(UnderlineSpan(), 0, mystring.length, 0)
//        tv_register2!!.text = content

//        tv_register2!!.setOnClickListener {
        //            val browserIntent = Intent(this, YandexWebView::class.java)
//            val url = "file:///android_asset/rules.html"
//            browserIntent.putExtra("url", url)
//            startActivity(browserIntent)
//        }

//        tv_register4!!.setOnClickListener {
        //            val browserIntent = Intent(this, YandexWebView::class.java)
//                                String url = "file:///android_asset/politika.html";
//            val url = "https://winstrike.gg/WinstrikePrivacyPolicy.pdf"
//            browserIntent.putExtra("url", url)
//            startActivity(browserIntent)
//        }

/*        val textFooter = "Политикой конфиденциальности"
        val content4 = SpannableString(textFooter)
        content4.setSpan(UnderlineSpan(), 0, textFooter.length, 0)
        tv_register4!!.text = content4*/
    }

}

