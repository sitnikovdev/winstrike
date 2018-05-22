package ru.prsolution.winstrike.ui.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.login.UserConfirmActivity;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import timber.log.Timber;

public class YandexWebView extends AppCompatActivity {

    private WebView mWebView;
    private String url;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_yandexpay);

        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
            finish();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (url.contains("politika.html")) {
            toolbar.setTitle(R.string.politica);
        } else {
            toolbar.setTitle(R.string.arena_name);
        }

        toolbar.setTitleMargin(250, 0, 0, 0);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_primary));
        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                presenter.onBackPressed();
                if (url.contains("politika.html") || url.contains("rules.html")) {
                    startActivity(new Intent(getApplicationContext(), UserConfirmActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                }
            }
        });

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setHorizontalScrollBarEnabled(false);
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        mWebView.loadUrl(url);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Timber.d("Load link: %s",url);
            if (url.equals("https://dev.winstrike.ru/api/v1/orders")) {
                Intent intent = new Intent();
                intent.putExtra("payments", true);
                startActivity(new Intent(YandexWebView.this,MainScreenActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


}
