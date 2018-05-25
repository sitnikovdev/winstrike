package ru.prsolution.winstrike.mvp.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.mvp.views.PayView;
import ru.terrakok.cicerone.Router;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ennur on 6/25/16.
 */
@InjectViewState
public class PayPresenter extends MvpPresenter<PayView> {
    private final Service service;
    private Router router;
    String url;
    private CompositeSubscription subscriptions;

/*
        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
//            finish();
        }

        if (url.contains("politika.html")) {
            toolbar.setTitle(R.string.politica);
        } else {
            toolbar.setTitle(R.string.arena_name);
        }
*/

    public PayPresenter(Service service, Router router, String url) {
        this.url = url;
        this.service = service;
        this.router = router;
        this.subscriptions = new CompositeSubscription();
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }

    public void loadUrl() {
        getViewState().loadUrl(url);
    }

    public void showProgress() {
        getViewState().showWait();
    }

    public void hideProgress() {
        getViewState().removeWait();
    }

    public void onBackPressed() {
        router.replaceScreen(Screens.MAP_SCREEN, 0);
    }
}
