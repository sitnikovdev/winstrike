package ru.prsolution.winstrike.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.views.PlacesView;
import ru.prsolution.winstrike.networking.Service;
import ru.terrakok.cicerone.Router;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by terrakok 26.11.16
 */

@InjectViewState
public class PlacesPresenter extends MvpPresenter<PlacesView> {
    private CompositeSubscription subscriptions;
    private final Service service;
    private Router router;
    private ArrayList<OrderModel> orderModels;

    public PlacesPresenter(Service service, Router router, ArrayList<OrderModel> orderModels) {
        this.subscriptions = new CompositeSubscription();
        this.service = service;
        this.router = router;
        this.orderModels = orderModels;
    }



    public void onBackPressed() {
        router.exit();
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }

    public List<OrderModel> getOrders() {
        return orderModels;
    }
}
