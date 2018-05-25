package ru.prsolution.winstrike.mvp.presenters;

import ru.prsolution.winstrike.mvp.models.LoginModel;
import ru.prsolution.winstrike.mvp.models.MessageResponse;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.views.RegisterView;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RegisterPresenter {
    private final Service service;
    private final RegisterView view;
    private CompositeSubscription subscriptions;

    public RegisterPresenter(Service service, RegisterView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void createUser(LoginModel user) {

        Subscription subscription = service.createUser(new Service.RegisterCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                view.onRegisterSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.onRegisterFailure(networkError.getAppErrorMessage());
            }

        }, user);

        subscriptions.add(subscription);
    }


    /**
     *  Send code by tap on "Again button send"
     * @param smsModel
     */
    public void sendSms(ConfirmSmsModel smsModel) {

        Subscription subscription = service.sendSmsByUserRequest(new Service.SmsCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                view.onSendSmsSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.onSmsSendFailure(networkError.getAppErrorMessage());
            }

        }, smsModel);

        subscriptions.add(subscription);
    }



    public void onStop() {
        subscriptions.unsubscribe();
    }

}
