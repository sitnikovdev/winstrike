package ru.prsolution.winstrike.mvp.views;


import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.prsolution.winstrike.ui.login.model.MessageResponse;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;

/**
 * Created by ennur on 6/25/16.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface SignInView {

    @StateStrategyType(SingleStateStrategy.class)
    void showWait();

    void removeWait();

    void onAuthFailure(String appErrorMessage);

    void onAuthResponseSuccess(AuthResponse authResponse);

    void onSendSmsSuccess(MessageResponse confirmModel);

    void onSmsSendFailure(String appErrorMessage);
}
