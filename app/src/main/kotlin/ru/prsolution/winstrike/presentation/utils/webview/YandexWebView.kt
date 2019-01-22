package ru.prsolution.winstrike.presentation.utils.webview

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar_text
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.common.utils.AuthUtils
import ru.prsolution.winstrike.presentation.login.SignInActivity
import ru.prsolution.winstrike.presentation.main.MainScreenActivity
import timber.log.Timber

class YandexWebView : AppCompatActivity() {

	private var mWebView: WebView? = null
	private var url: String? = null
	private var progressBar: ProgressBar? = null
	private var toolbar: Toolbar? = null

	private var confirmScreen: Intent? = null
	private var mainScreen: Intent? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.ac_yandexpay)

		progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
		toolbar = findViewById<View>(R.id.toolbar) as Toolbar
		setSupportActionBar(toolbar)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		confirmScreen = Intent(this, SignInActivity::class.java)
		mainScreen = Intent(this, MainScreenActivity::class.java)

		confirmScreen!!.putExtra("phone", AuthUtils.phone)

		url = intent.getStringExtra("url")

		if (TextUtils.isEmpty(url)) {
			finish()
		}

		if (url!!.contains("politika.html")) {
			initMainToolbar(true, resources.getString(R.string.politica))
		} else if (url!!.contains("rules")) {
			initMainToolbar(true, "Условия использования приложения Winstrike")
		} else {
			initMainToolbar(true, "Оплата")
		}


		toolbar!!.setNavigationOnClickListener {
			if (url!!.contains("politika.html") || url!!.contains("rules.html")) {
				startActivity(confirmScreen)
			} else {
				startActivity(mainScreen)
			}
		}


		mWebView = findViewById<View>(R.id.webView) as WebView
		mWebView!!.webViewClient = MyWebViewClient()
		mWebView!!.isHorizontalScrollBarEnabled = false
		// включаем поддержку JavaScript
		mWebView!!.settings.javaScriptEnabled = true
		mWebView!!.settings.loadWithOverviewMode = true
		mWebView!!.settings.useWideViewPort = true
		// указываем страницу загрузки
		mWebView!!.loadUrl(url)
	}


	private inner class MyWebViewClient : WebViewClient() {
		override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
			view.loadUrl(url)
			return true
		}

		override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
			super.onPageStarted(view, url, favicon)
			progressBar!!.visibility = View.VISIBLE
		}

		override fun onPageFinished(view: WebView, url: String) {
			super.onPageFinished(view, url)
			progressBar!!.visibility = View.GONE
		}

		override fun onLoadResource(view: WebView, url: String) {
			super.onLoadResource(view, url)
			Timber.d("Load link: %s", url)
			if (url == "https://dev.winstrike.ru/api/v1/orders") {
				val intent = Intent()
				intent.putExtra("payments", true)
				startActivity(Intent(this@YandexWebView, MainScreenActivity::class.java))
			}
		}
	}

	override fun onBackPressed() {
		if (url!!.contains("politika.html") || url!!.contains("rules.html")) {
			startActivity(confirmScreen)
		} else {
			startActivity(mainScreen)
		}
	}

	fun initMainToolbar(hide_menu: Boolean?, title: String) {
		setSupportActionBar(toolbar)
		invalidateOptionsMenu() // now onCreateOptionsMenu(...) is called again
		toolbar!!.setNavigationIcon(R.drawable.ic_back_arrow)
		toolbar_text!!.text = title
		toolbar!!.setNavigationIcon(R.drawable.ic_back_arrow)
		toolbar!!.setContentInsetsAbsolute(0, toolbar!!.contentInsetStartWithNavigation)
		supportActionBar!!.setDisplayShowTitleEnabled(false)
	}

	override fun onDestroy() {
		try {
			mWebView!!.destroy()
		} catch (ex: Exception) {
			ex.printStackTrace()
		}

		super.onDestroy()
	}

}
