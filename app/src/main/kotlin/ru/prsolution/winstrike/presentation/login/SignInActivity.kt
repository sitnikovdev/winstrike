package ru.prsolution.winstrike.presentation.login

import android.app.Activity
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.ac_login.et_password
import kotlinx.android.synthetic.main.ac_login.et_phone
import kotlinx.android.synthetic.main.ac_login.text_button_title
import kotlinx.android.synthetic.main.ac_login.text_footer
import kotlinx.android.synthetic.main.ac_login.text_footer2
import kotlinx.android.synthetic.main.ac_login.text_pol2
import kotlinx.android.synthetic.main.ac_login.text_pol4
import kotlinx.android.synthetic.main.ac_login.v_button
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.presentation.utils.webview.YandexWebView
import ru.prsolution.winstrike.common.utils.AuthUtils
import ru.prsolution.winstrike.common.utils.TextFormat
import ru.prsolution.winstrike.databinding.AcLoginBinding
import ru.prsolution.winstrike.domain.models.LoginViewModel
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.mvp.views.SignInView
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.presentation.help.HelpActivity
import ru.prsolution.winstrike.ui.Screens
import ru.prsolution.winstrike.presentation.main.MainScreenActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import ru.terrakok.cicerone.commands.SystemMessage
import timber.log.Timber
import ru.prsolution.winstrike.common.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot1Color
import ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot2Color
import ru.prsolution.winstrike.common.utils.Utils.setBtnEnable
import ru.prsolution.winstrike.datasource.model.AuthResponse
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel

/*
 * Created by oleg on 31.01.2018.
 */
// TODO: 13/05/2018 Reorder method call in this activity.
class SignInActivity : AppCompatActivity(), SignInView {

	private var loginViewModel: LoginViewModel? = null
	private var mProgressDialog: ProgressDialog? = null


	//    @Inject
	//    Router router;

	//    @Inject
	//    NavigatorHolder navigatorHolder;

	var mService: Service? = null

	//    @InjectPresenter
	internal var mSignInPresenter: SignInPresenter? = null

	private val navigator = object : Navigator {

		override fun applyCommands(commands: Array<Command>) {
			for (command in commands) applyCommand(command)
		}

		private fun applyCommand(command: Command) {
			if (command is Forward) {
				forward(command)
			} else if (command is Replace) {
				replace(command)
			} else if (command is Back) {
				back()
			} else if (command is SystemMessage) {
				//                Toast.makeText(StartActivity.this, ((SystemMessage) command).getMessage(), Toast.LENGTH_SHORT).show();
			} else {
				Log.e("Cicerone", "Illegal command for this screen: " + command.javaClass.simpleName)
			}
		}

		private fun forward(command: Forward) {
			when (command.screenKey) {

			}/*                case Screens.START_ACTIVITY_SCREEN:
                    startActivity(new Intent(StartActivity.this, StartActivity.class));
                    break;
                case Screens.MAIN_ACTIVITY_SCREEN:
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    break;
                case Screens.BOTTOM_NAVIGATION_ACTIVITY_SCREEN:
                    startActivity(new Intent(StartActivity.this, BottomNavigationActivity.class));
                    break;
                case Screens.PROFILE_SCREEN:
                    startActivity(new Intent(StartActivity.this, ProfileActivity.class));
                    break;
                default:
                    Log.e("Cicerone", "Unknown screen: " + command.getScreenKey());
                    break;*/
		}

		private fun replace(command: Replace) {
			if (command.screenKey == Screens.START_SCREEN) {
				startActivity(Intent(this@SignInActivity, MainScreenActivity::class.java))
			} else {
				Log.e("Cicerone", "Unknown screen: " + command.screenKey)
			}
		}

		private fun back() {
			finish()
		}
	}

	//    @ProvidePresenter
	fun createSignInPresenter(): SignInPresenter {
		return SignInPresenter(mService, this)
	}

	public override fun onCreate(savedInstanceState: Bundle?) {
		WinstrikeApp.instance.appComponent?.inject(this)
		super.onCreate(savedInstanceState)

		renderView()
		init()

	}

	fun init() {
		TextFormat.formatText(et_phone, "(___) ___-__-__")

		setBtnEnable(v_button, true)

		v_button!!.setOnClickListener { it ->
			if (et_phone!!.text.length >= 14 && et_password!!.text.length >= 6) {
				loginViewModel!!.username = formatPhone(et_phone!!.text.toString())
				loginViewModel!!.password = et_password!!.text.toString()
				AuthUtils.phone = loginViewModel!!.username.toString()
				AuthUtils.password = loginViewModel!!.password.toString()

				mSignInPresenter!!.signIn(loginViewModel!!)

			} else if (TextUtils.isEmpty(et_phone!!.text)) {
				toast("Введите номер телефона")
			} else if (TextUtils.isEmpty(et_password!!.text)) {
				toast("Пароль не должен быть пустым")
			} else if (et_phone!!.text.length < 14) {
				toast("Номер телефона не верный")
			} else if (et_password!!.text.length < 6) {
				toast("Длина пароля должна не менее 6 символов")
			}
		}

		//        checkFieldEnabled(et_phone, et_password, v_button);

		text_button_title!!.setOnClickListener { it -> startActivity(Intent(this, HelpActivity::class.java)) }

		setFooter()
	}

	private fun setFooter() {
		setTextFoot1Color(text_footer!!, "Еще нет аккаунта?", "#9b9b9b")
		setTextFoot2Color(text_footer2!!, " Зарегистрируйтесь", "#c9186c")
		text_footer2!!.setOnClickListener { it -> startActivity(Intent(this, SingUpActivity::class.java)) }

		val mystring = "Условиями"
		val content = SpannableString(mystring)
		content.setSpan(UnderlineSpan(), 0, mystring.length, 0)
		text_pol2!!.text = content


		text_pol2!!.setOnClickListener { it ->
			val browserIntent = Intent(this, YandexWebView::class.java)
			val url = "file:///android_asset/rules.html"
			browserIntent.putExtra("url", url)
			startActivity(browserIntent)
		}

		text_pol4!!.setOnClickListener { it ->
			val browserIntent = Intent(this, YandexWebView::class.java)
			val url = "file:///android_asset/politika.html"
			browserIntent.putExtra("url", url)
			startActivity(browserIntent)
		}

		val textFooter = "Политикой конфиденциальности"
		val content4 = SpannableString(textFooter)
		content4.setSpan(UnderlineSpan(), 0, textFooter.length, 0)
		text_pol4!!.text = content4

	}


	fun renderView() {
		//  setContentView(R.layout.ac_login);

		loginViewModel = LoginViewModel()

		val binding = DataBindingUtil.setContentView<AcLoginBinding>(this, R.layout.ac_login)

		binding.loginModel = loginViewModel

	}

	override fun showWait() {
		//        showProgressDialog();
	}

	override fun removeWait() {
		//        hideProgressDialog();
	}


	override fun onSendSmsSuccess(confirmModel: MessageResponse) {
		Timber.tag("common").d("Sms send success: %s", confirmModel.message)
		//        toast("Код выслан повторно");
	}

	override fun onSmsSendFailure(appErrorMessage: String) {
		Timber.tag("common").w("Sms send error: %s", appErrorMessage)
		if (appErrorMessage.contains("404"))
			toast("Ошибка отправки кода! Нет пользователя с таким номером")
		if (appErrorMessage.contains("409")) toast("Ошибка функции кодогенерации")
		if (appErrorMessage.contains("422")) toast("Не указан номер телефона")
	}

	/**
	 * Success auth user. Save token
	 *
	 * @param authResponse - (token,isConfirmed)
	 */
	override fun onAuthResponseSuccess(authResponse: AuthResponse) {
		val confirmed = authResponse.user?.confirmed

		updateUser(authResponse)

		if (confirmed!!) {
			//                        router.replaceScreen(Screens.START_SCREEN);
			startActivity(Intent(this, MainScreenActivity::class.java))
			Timber.d("Success signIn")
		} else {
			val smsModel = ConfirmSmsModel()
			smsModel.username = authResponse.user!!.phone
			mSignInPresenter!!.sendSms(smsModel)

			val intent = Intent(this, UserConfirmActivity::class.java)
			intent.putExtra("phone", smsModel.username)
			startActivity(intent)
		}

	}

	private fun updateUser(authResponse: AuthResponse) {
		AuthUtils.name = authResponse.user?.name!!
		AuthUtils.token = authResponse.token.toString()
		AuthUtils.phone = authResponse.user!!.phone!!
		AuthUtils.isConfirmed = authResponse.user!!.confirmed!!
		AuthUtils.publicid = authResponse.user!!.publicId!!
	}

	override fun onAuthFailure(appErrorMessage: String) {
		Timber.e("Error on auth: %s", appErrorMessage)
		if (appErrorMessage.contains("403")) toast("Неправильный пароль")
		if (appErrorMessage.contains("404")) {
			toast("Пользователь не найден")
		}
		if (appErrorMessage.contains("502")) toast("Ошибка сервера")
		if (appErrorMessage.contains("No Internet Connection!"))
			toast("Интернет подключение не доступно!")

	}

	override fun onResume() {
		super.onResume()
		if (mSignInPresenter == null) {
			mSignInPresenter = createSignInPresenter()
		}
		//        navigatorHolder.setNavigator(navigator);
	}

	override fun onPause() {
		super.onPause()
	}


	fun showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog(this)
			mProgressDialog!!.setMessage("Авторизация...")
			mProgressDialog!!.isIndeterminate = true
		}

		mProgressDialog!!.show()
	}

	protected fun hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog!!.isShowing) {
			mProgressDialog!!.dismiss()
		}
	}


	/*
    protected void checkFieldEnabled(EditText et_phone, EditText et_pass, View button) {
        Observable<TextViewTextChangeEvent> phoneObservable = RxTextView.textChangeEvents(et_phone);
        Observable<TextViewTextChangeEvent> passwordObservable = RxTextView.textChangeEvents(et_pass);
        Observable.combineLatest(phoneObservable, passwordObservable, (phoneSelected, passwordSelected) -> {
            boolean phoneCheck = phoneSelected.text().length() >= 14;
            boolean passwordCheck = passwordSelected.text().length() >= 4;
            return phoneCheck && passwordCheck;
        }).subscribe(aBoolean -> {
            if (aBoolean) {
                setBtnEnable(button, true);
            } else {
                setBtnEnable(button, false);
            }
        });
    }
*/

	protected fun toast(message: String) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
	}

	override fun onStop() {
		super.onStop()
		hideProgressDialog()
		mSignInPresenter!!.onStop()
		mSignInPresenter = null
	}

	override fun onBackPressed() {
		//        super.onBackPressed();
		val intent = Intent(Intent.ACTION_MAIN)
		intent.addCategory(Intent.CATEGORY_HOME)
		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
		startActivity(intent)
		val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
		am.killBackgroundProcesses("ru.prsolution.winstrike")
		finish()
	}


	fun onLogin(view: View) {
		Timber.d("View on click fire")
	}

	/*    public class LoginHandler {

     *//*        public LoginHandler(LoginPresenter presenter) {
            this.presenter = presenter;
        }*//*

        public View.OnClickListener onLogin(final LoginViewModel viewModel) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    presenter.login(viewModel);
                    Timber.d("View on click fire");
                }
            };
        }

    }*/
}
