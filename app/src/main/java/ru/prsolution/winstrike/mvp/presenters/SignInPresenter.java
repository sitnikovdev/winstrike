package ru.prsolution.winstrike.mvp.presenters;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.common.logging.SignInModel;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.views.SignInView;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

@InjectViewState
public class SignInPresenter extends MvpPresenter<SignInView> {
    private final Service service;
    private Router router;
    private CompositeSubscription subscriptions;

    public SignInPresenter(Service service, Router router) {
        this.subscriptions = new CompositeSubscription();
        this.service = service;
        this.router = router;
    }

    public void signIn(SignInModel user) {
        getViewState().showWait();

        Subscription subscription = service.authUser(new Service.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                getViewState().removeWait();
                getViewState().onAuthResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().removeWait();
                getViewState().onAuthFailure(networkError.getAppErrorMessage());
            }

        }, user);

        subscriptions.add(subscription);
    }


    public void sendSms(ConfirmSmsModel smsModel) {
        getViewState().showWait();

        Subscription subscription = service.sendSms(new Service.SmsCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                getViewState().removeWait();
                getViewState().onSendSmsSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().removeWait();
                getViewState().onAuthFailure(networkError.getAppErrorMessage());
            }

        }, smsModel);

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
