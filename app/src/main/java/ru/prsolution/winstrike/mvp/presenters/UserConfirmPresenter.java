package ru.prsolution.winstrike.mvp.presenters;


import ru.prsolution.winstrike.ui.login.model.ConfirmModel;
import ru.prsolution.winstrike.ui.login.model.MessageResponse;
import ru.prsolution.winstrike.ui.login.model.ProfileModel;
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
//        view.showWait();

        Subscription subscription = service.confirmUser(new Service.ConfirmCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
//                view.removeWait();
                view.onUserConfirmSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
//                view.removeWait();
                view.onUserConfirmFailure(networkError.getAppErrorMessage());
            }

        }, sms_code,confirmPhone);

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


    /**
     *  Update user profile after successfully confirmed. Set user name.
     *
     * @param token  saved token
     * @param profile  profile user data
     * @param publicId  user public id
     */
    public void updateProfile(String token, ProfileModel profile, String publicId) {

        Subscription subscription = service.updateUser(new Service.ProfileCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                view.onProfileUpdateSuccessfully(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.onFailtureUpdateProfile(networkError.getAppErrorMessage());
            }

        },token, profile, publicId);

        subscriptions.add(subscription);
    }



    public void onStop() {
        subscriptions.unsubscribe();
    }
}
