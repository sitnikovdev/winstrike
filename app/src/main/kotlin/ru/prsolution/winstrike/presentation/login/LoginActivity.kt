package ru.prsolution.winstrike.presentation.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.toolbar.*
import ru.prsolution.winstrike.presentation.NavigationListener
import android.content.Intent




/*
 * Created by oleg on 31.01.2018.
 */

interface FooterSetUp {
    fun setRegisterLoginFooter(textView: TextView, action: NavDirections)
}

class LoginActivity : AppCompatActivity() {
/*

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ru.prsolution.winstrike.R.layout.ac_login)


//        Navigation
//        navController = Navigation.findNavController(this@LoginActivity, ru.prsolution.winstrike.R.id.login_host_fragment)


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
 fun setRegisterLoginFooter(textView: TextView, action: NavDirections) {
//       Уже есть аккуунт? Войдите
        val register = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_register_message_enter))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
//                val action = RegisterFragmentDirections.actionToNavigationLogin()
                navigate(action)
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = register
    }

    // Fragment navigation
     fun navigate(action: NavDirections) {
//        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.login_host_fragment).navigate(action)
    }


    fun exitAppMethod() {

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitAppMethod()
    }
*/

}
