package ru.prsolution.winstrike.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.Map;

import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.mvp.views.ChooseView;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by terrakok 26.11.16
 */

@InjectViewState
public class ChoosePresenter extends MvpPresenter<ChooseView> {
    private CompositeSubscription subscriptions;
    private final Service service;
    private Router router;
    private int number;


    public ChoosePresenter(Service service, Router router) {
        this.subscriptions = new CompositeSubscription();
        this.service = service;
        this.router = router;

        // Date time
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
//        getActivePid();
    }

    public void getActivePid() {
        getViewState().showWait();

        Subscription subscription = service.getActivePid(new Service.RoomsCallback() {
            @Override
            public void onSuccess(Rooms authResponse) {
                getViewState().removeWait();
                getViewState().onGetActivePidResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().removeWait();
                getViewState().onGetAcitivePidFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }



    public void getArenaByTimeRange(String activeLayoutPid, Map<String,String> time) {
        getViewState().showWait();

        Subscription subscription = service.getArenaByTimeRange(new Service.RoomLayoutByTimeCallback() {
            @Override
            public void onSuccess(RoomLayoutFactory authResponse) {
                getViewState().removeWait();
                getViewState().onGetArenaByTimeResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().removeWait();
                getViewState().onGetArenaByTimeFailure(networkError.getAppErrorMessage());
            }

        },activeLayoutPid,time);

        subscriptions.add(subscription);
    }


    public void onForwardPressed() {
        router.navigateTo(Screens.FORWARD_SCREEN, number + 1);
    }

    public void onGithubPressed() {
        router.navigateTo(Screens.GITHUB_SCREEN);
    }

    public void onBackPressed() {
//        router.exit();
        router.replaceScreen(Screens.START_SCREEN);
    }

    public void onNextButtonClick() {
       router.navigateTo(Screens.MAP_SCREEN,number+1);
    }


    public void onStop() {
        subscriptions.unsubscribe();
    }

}
