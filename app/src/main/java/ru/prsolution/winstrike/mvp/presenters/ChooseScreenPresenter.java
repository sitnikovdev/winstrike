package ru.prsolution.winstrike.mvp.presenters;


import java.util.Map;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.mvp.apimodels.Arenas;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.main.ChooseScreenFragment;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class ChooseScreenPresenter {
    private CompositeSubscription subscriptions;
    private final Service service;
    private ChooseScreenFragment fragment;


    public ChooseScreenPresenter(Service service, ChooseScreenFragment fragment) {
        this.subscriptions = new CompositeSubscription();
        if (service == null) {
            this.service = WinstrikeApp.getInstance().getService();
        } else {
            this.service = service;
        }
        this.fragment = fragment;
    }


    public void getActiveArenaPid() {
        fragment.showWait();

        Subscription subscription = service.getRooms(new Service.RoomsCallback() {
            @Override
            public void onSuccess(Rooms authResponse) {
                fragment.onGetActivePidResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                fragment.removeWait();
                fragment.onGetAcitivePidFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }

    public void getActiveArena() {
        fragment.showWait();

        Subscription subscription = service.getArenas(new Service.ArenasCallback() {
            @Override
            public void onSuccess(Arenas authResponse) {
                fragment.onGetArenasResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                fragment.removeWait();
                fragment.onGetAcitivePidFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }


    public void getArenaByTimeRange(String activeLayoutPid, Map<String, String> time) {

        Subscription subscription = service.getArenaByTimeRange(new Service.RoomLayoutByTimeCallback() {
            @Override
            public void onSuccess(RoomLayoutFactory authResponse) {
                fragment.removeWait();
                fragment.onGetArenaByTimeResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                fragment.removeWait();
                fragment.onGetArenaByTimeFailure(networkError.getAppErrorMessage());
            }

        }, activeLayoutPid, time);

        subscriptions.add(subscription);
    }


    public void onStop() {
        subscriptions.unsubscribe();
    }

}
