package ru.prsolution.winstrike.presentation.payment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_main.webView
import kotlinx.android.synthetic.main.fmt_pay.progressBar
import kotlinx.android.synthetic.main.fmt_pay.toolbar
import kotlinx.android.synthetic.main.fmt_pay.toolbar_text
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.common.BackButtonListener
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.presentation.utils.Constants
import timber.log.Timber

class PayScreenFragment : Fragment(),  BackButtonListener {

	internal var service: Service? = null

	internal var presenter: PayPresenter? = null


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fmt_pay, container, false)
		toolbar_text.text = "Оплата"
		initToolbar("Оплата", R.drawable.ic_back_arrow)
		initWebView()
		presenter!!.loadUrl()
		return view
	}

	private fun initToolbar(s: String, navIcon: Int) {
		if (navIcon != 0) {
			toolbar!!.setNavigationIcon(navIcon)
			toolbar!!.setContentInsetsAbsolute(0, toolbar!!.contentInsetStartWithNavigation)
		}
		toolbar_text!!.text = s

		toolbar!!.setNavigationOnClickListener { view -> presenter!!.onBackPressed() }
	}


	private fun initWebView() {
		webView!!.webViewClient = MyWebViewClient()
		webView!!.isHorizontalScrollBarEnabled = false
		webView!!.settings.javaScriptEnabled = true
	}


	private inner class MyWebViewClient : WebViewClient() {
		override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
			view.loadUrl(url)
			return true
		}

		override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
			super.onPageStarted(view, url, favicon)
			presenter!!.showProgress()
		}


		override fun onPageFinished(view: WebView, url: String) {
			super.onPageFinished(view, url)
			presenter!!.hideProgress()
		}

		override fun onLoadResource(view: WebView, url: String) {
			super.onLoadResource(view, url)
			Timber.d("Load link: %s", url)
			if (url == Constants.ORDER_URL) {
				val intent = Intent()
				intent.putExtra("payments", true)
				//                startActivity(new Intent(getActivity(), MainScreenActivity.class));
			}
		}
	}

	override fun onBackPressed(): Boolean {
		return false
	}

	 fun showWait() {
		progressBar!!.visibility = View.VISIBLE
	}

	 fun removeWait() {
		progressBar!!.visibility = View.GONE
	}

	 fun loadUrl(url: String) {
		webView!!.loadUrl(url)
	}

	override fun onStop() {
		super.onStop()
		presenter!!.onStop()
	}

	companion object {


		private val EXTRA_NAME = "extra_name"
		private val URL = "mService url"

		//    @ProvidePresenter
		/*    PayPresenter provideMainScreenPresenter() {
        return new PayPresenter(service,
                ((RouterProvider) getParentFragment()).getRouter(),
                getArguments().getString(URL)
        );
    }*/

		fun getNewInstance(name: String, url: String): PayScreenFragment {
			val fragment = PayScreenFragment()
			val arguments = Bundle()
			arguments.putString(EXTRA_NAME, name)
			arguments.putString(URL, url)
			//        fragment.setArguments(arguments);
			return fragment
		}
	}
}
