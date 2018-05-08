package ru.prsolution.winstrike.mvp.views;

import android.widget.ImageView;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.SeatApi;
import ru.prsolution.winstrike.mvp.models.GameRoom;
import ru.prsolution.winstrike.mvp.models.LabelRoom;
import ru.prsolution.winstrike.mvp.models.Seat;

/**
 * Created by terrakok 26.11.16
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MapView extends MvpView {

    void showLabel(List<LabelRoom> labels);

    void showSeat(GameRoom room);


    @StateStrategyType(OneExecutionStateStrategy.class)
    void onSnackBarShow();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onSnackBarHide();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onScreenInit();

    void showWait();

    void removeWait();

    void onGetPaymentResponseSuccess(PaymentResponse authResponse);

    void onGetPaymentFailure(String appErrorMessage);
}
