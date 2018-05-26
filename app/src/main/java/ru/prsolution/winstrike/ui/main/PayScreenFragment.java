package ru.prsolution.winstrike.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.common.BackButtonListener;
import ru.prsolution.winstrike.common.RouterProvider;
import ru.prsolution.winstrike.mvp.presenters.PayPresenter;
import ru.prsolution.winstrike.mvp.views.PayView;
import timber.log.Timber;

public class PayScreenFragment extends MvpAppCompatFragment implements PayView, BackButtonListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_text)
    TextView tvToolbarTitle;


    private static final String EXTRA_NAME = "extra_name";
    private static final String URL = "mService url";

    @Inject
    Service service;

    @InjectPresenter
    PayPresenter presenter;

    @ProvidePresenter
    PayPresenter provideMainScreenPresenter() {
        return new PayPresenter(service,
                ((RouterProvider) getParentFragment()).getRouter(),
                getArguments().getString(URL)
        );
    }

    public static PayScreenFragment getNewInstance(String name, String url) {
        PayScreenFragment fragment = new PayScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putString(URL, url);
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_pay, container, false);
        ButterKnife.bind(this, view);
        tvToolbarTitle.setText("Оплата");
        initToolbar("Оплата",R.drawable.ic_back_arrow);
        initWebView();
        presenter.loadUrl();
        return view;
    }

    private void initToolbar( String s, int navIcon) {
        if (navIcon != 0) {
            toolbar.setNavigationIcon(navIcon);
            toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        }
        tvToolbarTitle.setText(s);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackPressed();
            }
        });
    }


    private void initWebView() {
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
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
            presenter.showProgress();
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            presenter.hideProgress();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Timber.d("Load link: %s", url);
            if (url.equals("https://dev.winstrike.ru/api/v1/orders")) {
                Intent intent = new Intent();
                intent.putExtra("payments", true);
                startActivity(new Intent(getActivity(), MainScreenActivity.class));
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }
}
