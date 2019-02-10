package ru.prsolution.winstrike.presentation.payment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_yandex_web_view.pbar
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.main.MainViewModel
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-01-27
 */

class YandexWebViewFragment : Fragment() {
    private var mWebView: WebView? = null
    private var url: String? = null
    private var mVm: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVm = activity?.let { ViewModelProviders.of(it)[MainViewModel::class.java] }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_yandex_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbar.visibility = View.VISIBLE

        arguments?.let {
            val safeArgs = YandexWebViewFragmentArgs.fromBundle(it)
            url = safeArgs.url
        }

/*        activity?.let {
            mVm?.redirectUrl?.observe(it, Observer {
                Timber.d("url: $url")
                url = it
            })
        }*/

        mWebView = view.findViewById<View>(R.id.webView) as WebView
        mWebView?.webViewClient = MyWebViewClient()
        mWebView?.isHorizontalScrollBarEnabled = false
        // включаем поддержку JavaScript
        // (TODO: Remove if don't need it. Introduce XSS vulnerabilities.)
        mWebView?.settings?.javaScriptEnabled = true
        mWebView?.settings?.loadWithOverviewMode = true
        mWebView?.settings?.useWideViewPort = true
        // указываем страницу загрузки
        if (!TextUtils.isEmpty(url)) {
            mWebView?.loadUrl(url)
        }
    }

    private inner class MyWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            pbar.visibility = View.GONE
        }

        override fun onLoadResource(view: WebView, url: String) {
            super.onLoadResource(view, url)
            Timber.d("Load link: %s", url)
            if (url == "https://dev.winstrike.ru/api/v1/orders") {
// 				val intent = Intent()
// 				intent.putExtra("payments", true)
// 				startActivity(Intent(this@YandexWebView, MainActivity::class.java))
            }
        }
    }
}
