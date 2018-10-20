package ru.prsolution.winstrike.mvp.presenters;


import ru.prsolution.winstrike.mvp.apimodels.Arenas;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.models.LoginViewModel;
import ru.prsolution.winstrike.mvp.models.MessageResponse;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.start.SplashActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SplashPresenter {
    private final Service service;
    private final SplashActivity activity;
    private CompositeSubscription subscriptions;

    public SplashPresenter(Service service, SplashActivity activity) {
        this.subscriptions = new CompositeSubscription();
        this.service = service;
        this.activity = activity;
    }

    public void signIn(LoginViewModel user) {
//        activity.showWait();

        Subscription subscription = service.authUser(new Service.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
//                activity.removeWait();
                activity.onAuthResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
//                activity.removeWait();
                activity.onAuthFailure(networkError.getAppErrorMessage());
            }

        }, user);

        subscriptions.add(subscription);
    }


    public void sendSms(ConfirmSmsModel smsModel) {

        Subscription subscription = service.sendSmsByUserRequest(new Service.SmsCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
//                activity.removeWait();
                activity.onSendSmsSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
//                activity.removeWait();
                activity.onSendSmsFailure(networkError.getAppErrorMessage());
            }

        }, smsModel);

        subscriptions.add(subscription);
    }


    public void getActiveArena() {

        Subscription subscription = service.getArenas(new Service.ArenasCallback() {
            @Override
            public void onSuccess(Arenas authResponse) {
                activity.onGetArenasResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                activity.onGetActivePidFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }




    public void onStop() {
        subscriptions.unsubscribe();
    }
}
