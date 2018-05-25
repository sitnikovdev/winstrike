package ru.prsolution.winstrike.mvp.views;


import ru.prsolution.winstrike.ui.login.model.MessageResponse;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;

public interface RegisterView {
    void showWait();

    void removeWait();

    void onRegisterFailure(String appErrorMessage);

    void onRegisterSuccess(AuthResponse authResponse);

    void onSendSmsSuccess(MessageResponse authResponse);

    void onSmsSendFailure(String appErrorMessage);
}
