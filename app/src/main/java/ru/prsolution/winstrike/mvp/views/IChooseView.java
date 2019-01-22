package ru.prsolution.winstrike.mvp.views;

import ru.prsolution.winstrike.datasource.model.Arenas;
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory;


public interface IChooseView {

    void showWait();

    void removeWait();

    void onGetArenasResponseSuccess(Arenas authResponse, int selectedArena);

    void onGetAcitivePidFailure(String appErrorMessage);

    void onDateClickListener();

    void onTimeClickListener();

    void onNextButtonClickListener();


    void onGetArenaByTimeResponseSuccess(RoomLayoutFactory authResponse);

    void onGetArenaByTimeFailure(String appErrorMessage);
}
