package ru.prsolution.winstrike.mvp.views;


import com.arellomobile.mvp.MvpView;

/**
 * Created by ennur on 6/25/16.
 */
public interface PayView extends MvpView {
    void showWait();

    void removeWait();

    void loadUrl(String url);
}
