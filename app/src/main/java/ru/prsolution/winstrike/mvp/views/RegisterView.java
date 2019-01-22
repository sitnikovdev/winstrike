package ru.prsolution.winstrike.mvp.views;


import ru.prsolution.winstrike.datasource.model.AuthResponse;
import ru.prsolution.winstrike.domain.models.MessageResponse;

public interface RegisterView {
    void showWait();

    void removeWait();

    void onRegisterFailure(String appErrorMessage);

    void onRegisterSuccess(AuthResponse authResponse);

    void onSendSmsSuccess(MessageResponse authResponse);

    void onSmsSendFailure(String appErrorMessage);
}
