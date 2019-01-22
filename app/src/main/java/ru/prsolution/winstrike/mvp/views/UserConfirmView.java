package ru.prsolution.winstrike.mvp.views;


import ru.prsolution.winstrike.domain.models.MessageResponse;

/**
 * Created by ennur on 6/25/16.
 */
public interface UserConfirmView {
    void showWait();

    void removeWait();

    void onUserConfirmFailure(String appErrorMessage);

    void onUserConfirmSuccess(MessageResponse confirmModel);

    void onSendSmsSuccess(MessageResponse authResponse);

    void onSmsSendFailure(String appErrorMessage);

    void onProfileUpdateSuccessfully(MessageResponse authResponse);

    void onFailtureUpdateProfile(String appErrorMessage);
}
