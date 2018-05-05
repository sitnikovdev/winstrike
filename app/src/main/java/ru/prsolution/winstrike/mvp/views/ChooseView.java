package ru.prsolution.winstrike.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;

/**
 * Created by terrakok 26.11.16
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ChooseView extends MvpView {
    void setChainText(String chainText);

    void onDateSelect();

    void onTimeSelect();

    void onNextButtonClick();

    void showWait();

    void removeWait();

    void onGetActivePidResponseSuccess(Rooms authResponse);

    void onGetAcitivePidFailure(String appErrorMessage);


    void onGetArenaResponseSuccess(RoomLayoutFactory authResponse);

    void onGetArenaFailure(String appErrorMessage);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void onGetArenaByTimeResponseSuccess(RoomLayoutFactory authResponse);

    void onGetArenaByTimeFailure(String appErrorMessage);
}
