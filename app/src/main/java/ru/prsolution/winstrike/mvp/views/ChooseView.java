package ru.prsolution.winstrike.mvp.views;

import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;


public interface ChooseView {

    void showWait();

    void removeWait();

    void onGetActivePidResponseSuccess(Rooms authResponse);

    void onGetAcitivePidFailure(String appErrorMessage);


    void onGetArenaByTimeResponseSuccess(RoomLayoutFactory authResponse);

    void onGetArenaByTimeFailure(String appErrorMessage);
}
