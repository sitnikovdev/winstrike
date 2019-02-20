package ru.prsolution.winstrike.presentation.login

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.toolbar.*
import ru.prsolution.winstrike.presentation.login.register.RegisterFragmentDirections
import ru.prsolution.winstrike.presentation.utils.Constants.URL_CONDITION
import ru.prsolution.winstrike.presentation.utils.Constants.URL_POLITIKA
import android.view.KeyEvent
import ru.prsolution.winstrike.R


/*
 * Created by oleg on 31.01.2018.
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_login)


//        Navigation
        navController = Navigation.findNavController(this@LoginActivity, ru.prsolution.winstrike.R.id.login_host_fragment)

        navController.addOnDestinationChangedListener { nav, destination, _ ->
            /*            when (destination.id) {
                            R.id.navigation_home -> {bottomNavigation.show()
                                destination.label = PrefUtils.arenaName
                            }
                            R.id.navigation_order,
                            R.id.navigation_profile -> bottomNavigation.show()
                            else -> bottomNavigation.hide()
                        }*/
        }

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(toolbar)
        setupActionBar(navController, appBarConfiguration)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

//    Login Footer
    fun setRegisterLoginFooter(textView: TextView) {
//       Уже есть аккуунт? Войдите
        val register = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_register_message_enter))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = RegisterFragmentDirections.actionToNavigationLogin()
                navigate(action)
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = register
    }

    // Fragment navigation
    fun navigate(action: NavDirections) {
        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.login_host_fragment).navigate(action)
    }

    //    Login policy footer
    fun setLoginPolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.nextActionPolitika(URL_CONDITION)
                action.title = getString(ru.prsolution.winstrike.R.string.fmt_title_condition)
                Navigation.findNavController(this@LoginActivity, ru.prsolution.winstrike.R.id.login_host_fragment).navigate(action)
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.nextActionPolitika(URL_POLITIKA)
                action.title = getString(ru.prsolution.winstrike.R.string.fmt_login_title_politika)
                Navigation.findNavController(this@LoginActivity, ru.prsolution.winstrike.R.id.login_host_fragment).navigate(action)
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    //    Code policy footer
    fun setCodePolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    //    Name policy footer
    fun setNamePolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    // Phone hint and phone
    fun setPhoneHint(textView: TextView, phone: String?) {
        val phoneHint = SpannableString("Введите 6-значный код, который был\n" +
                "отправлен на номер $phone")
        phoneHint.setSpan(ForegroundColorSpan(Color.BLACK), phoneHint.length - 12, phoneHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = phoneHint
    }

}
