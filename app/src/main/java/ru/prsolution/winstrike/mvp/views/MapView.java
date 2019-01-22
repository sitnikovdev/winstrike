package ru.prsolution.winstrike.mvp.views;

import ru.prsolution.winstrike.datasource.model.PaymentResponse;
import ru.prsolution.winstrike.domain.models.GameRoom;

public interface MapView  {

    void showSeat(GameRoom room);

    void onSnackBarShow();

    void onSnackBarHide();

    void onScreenInit();

    void showWait();

    void removeWait();

    void onGetPaymentResponseSuccess(PaymentResponse authResponse);

    void onGetPaymentFailure(String appErrorMessage);
}
