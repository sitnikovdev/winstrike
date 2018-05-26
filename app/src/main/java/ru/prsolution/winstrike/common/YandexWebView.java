package ru.prsolution.winstrike.common;

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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.utils.AuthUtils;
import ru.prsolution.winstrike.ui.login.UserConfirmActivity;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import timber.log.Timber;

public class YandexWebView extends AppCompatActivity {

    private WebView mWebView;
    private String url;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @BindView(R.id.toolbar_text)
    TextView tvToolbarTitle;
    private Intent confirmScreen;
    private Intent mainScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_yandexpay);
        ButterKnife.bind(this);

        confirmScreen = new Intent(this, UserConfirmActivity.class);
        mainScreen = new Intent(this, MainScreenActivity.class);

        confirmScreen.putExtra("phone",AuthUtils.INSTANCE.getPhone());
        mainScreen.putExtra("phone",AuthUtils.INSTANCE.getPhone());

        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
            finish();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (url.contains("politika.html")) {
            initMainToolbar(true, getResources().getString(R.string.politica));
        } else if (url.contains("rules")) {
            initMainToolbar(true, "Условия использования приложения Winstrike");
        } else {
            initMainToolbar(true, "Оплата");
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url.contains("politika.html") || url.contains("rules.html")) {
                    startActivity(confirmScreen);
                } else {
                    startActivity(mainScreen);
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
            Timber.d("Load link: %s", url);
            if (url.equals("https://dev.winstrike.ru/api/v1/orders")) {
                Intent intent = new Intent();
                intent.putExtra("payments", true);
                startActivity(new Intent(YandexWebView.this, MainScreenActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (url.contains("politika.html") || url.contains("rules.html")) {
            startActivity(confirmScreen);
        } else {
            startActivity(mainScreen);
        }
/*        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }*/
    }

    public void initMainToolbar(Boolean hide_menu, String title) {
        setSupportActionBar(toolbar);
        invalidateOptionsMenu(); // now onCreateOptionsMenu(...) is called again
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        tvToolbarTitle.setText(title);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStartWithNavigation());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onDestroy() {
        try {
            mWebView.destroy();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

}
