package ru.prsolution.winstrike.presentation.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.ac_help.tv_help_centr
import kotlinx.android.synthetic.main.ac_help.tv_sms
import kotlinx.android.synthetic.main.inc_main_toolbar.toolbar
import kotlinx.android.synthetic.main.inc_main_toolbar.toolbar_title
import ru.prsolution.winstrike.R

/*
 * Created by oleg on 01.02.2018.
 */

class HelpActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.ac_help)

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayShowTitleEnabled(false)
		toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
//		        toolbar.setNavigationOnClickListener(
//		                it -> startActivity(new Intent(this, SignInActivity.class))
//		        );

		toolbar_title.setText(R.string.help_title)

		tv_sms.setOnClickListener { startActivity(Intent(this, HelpSmsActivity::class.java)) }

		tv_help_centr.setOnClickListener {
			val url = "https://winstrike.gg"
			val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
			startActivity(browserIntent)
			//startActivity(new Intent(this, HelpPasswordActivity.class));
		}
	}
}
