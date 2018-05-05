package ru.prsolution.winstrike.mvp.presenters;


import ru.prsolution.winstrike.common.logging.ConfirmModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.views.UserConfirmView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ennur on 6/25/16.
 */
public class UserConfirmPresenter {
    private final Service service;
    private final UserConfirmView view;
    private CompositeSubscription subscriptions;

    public UserConfirmPresenter(Service service, UserConfirmView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void confirmUser(String sms_code, ConfirmModel confirmPhone) {
        view.showWait();

        Subscription subscription = service.confirmUser(new Service.ConfirmCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                view.removeWait();
                view.onUserConfirmSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onUserConfirmFailure(networkError.getAppErrorMessage());
            }

        }, sms_code,confirmPhone);

        subscriptions.add(subscription);
    }

    public void sendSms(ConfirmSmsModel smsModel) {
        view.showWait();

        Subscription subscription = service.sendSms(new Service.SmsCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                view.removeWait();
                view.onSendSmsSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onSmsSendFailure(networkError.getAppErrorMessage());
            }

        }, smsModel);

        subscriptions.add(subscription);
    }


    public void onStop() {
        subscriptions.unsubscribe();
    }
}
