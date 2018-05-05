package ru.prsolution.winstrike.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.mvp.views.HomeView;
import ru.terrakok.cicerone.Router;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by terrakok 25.11.16
 */
@InjectViewState
public class HomePresenter extends MvpPresenter<HomeView> {
    private Router router;
    private final Service service;
    private CompositeSubscription subscriptions;

    public HomePresenter(Service service, Router router) {
        this.router = router;
        this.service = service;
        this.subscriptions = new CompositeSubscription();
    }

}
