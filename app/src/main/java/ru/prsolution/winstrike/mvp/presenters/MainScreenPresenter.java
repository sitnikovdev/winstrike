package ru.prsolution.winstrike.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.utils.Utils;
import ru.prsolution.winstrike.mvp.apimodels.Order;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.apimodels.Orders;
import ru.prsolution.winstrike.mvp.models.FCMModel;
import ru.prsolution.winstrike.mvp.models.MessageResponse;
import ru.prsolution.winstrike.mvp.models.ProfileModel;
import ru.prsolution.winstrike.mvp.views.MainScreenView;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.Screens;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.Utils.formatTime;

/**
 * Created by terrakok 25.11.16
 */
@InjectViewState
public class MainScreenPresenter extends MvpPresenter<MainScreenView> {
    private Router router;
    private final Service service;
    private CompositeSubscription subscriptions;

    public MainScreenPresenter(Service service, Router router) {
        this.router = router;
        this.service = service;
        this.subscriptions = new CompositeSubscription();
    }

    public void onCreate() { getViewState().highlightTab(MainScreenView.HOME_TAB_POSITION);
        router.replaceScreen(Screens.START_SCREEN);
    }


    public void onTabPlaceClick(List<OrderModel> mPayList) {
        getViewState().highlightTab(MainScreenView.PLACE_TAB_POSITION);
        router.replaceScreen(Screens.PLACE_SCREEN,mPayList);
    }

    public void onTabUserClick() {
        getViewState().highlightTab(MainScreenView.USER_TAB_POSITION);
        router.replaceScreen(Screens.USER_SCREEN);
    }

    public void onTabHomeClick() {
        getViewState().highlightTab(MainScreenView.HOME_TAB_POSITION);
        router.replaceScreen(Screens.START_SCREEN);
    }

    public void onMapShowClick() {
        router.replaceScreen(Screens.MAP_SCREEN,0);
    }

    public void onChooseScreenClick() {
        getViewState().hideBottomTab();
        router.replaceScreen(Screens.CHOOSE_SCREEN);
    }


    public void onBackPressed() {
//        router.exit(); // used Back command
        getViewState().showBottomTab();
        getViewState().setProfileScreenInterfaceVisibility(false);
        router.replaceScreen(Screens.START_SCREEN,0);
    }


    public void updateProfile(String token, ProfileModel profile, String publicId) {

        Subscription subscription = service.updateUser(new Service.ProfileCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
               getViewState().onProfileUpdateSuccessfully(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().onFailtureUpdateProfile(networkError.getAppErrorMessage());
            }

        },token, profile, publicId);

        subscriptions.add(subscription);
    }


    public void sendFCMTokenToServer(String token, FCMModel fcmToken) {

        Subscription subscription = service.sendToken(new Service.FcmTokenCallback() {
            @Override
            public void onSuccess(MessageResponse messageResponse) {
                onTokenSendSuccessfully(messageResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                onFailtureTokenSend(networkError.getAppErrorMessage());
            }
        }, token, fcmToken);

        subscriptions.add(subscription);
    }


    private void onFailtureTokenSend(String appErrorMessage) {
        Timber.d("On failure send token: %s", appErrorMessage);
    }

    private void onTokenSendSuccessfully(MessageResponse authResponse) {
        Timber.d("Successfully send token: %s", authResponse.getMessage());
    }


    public void getOrders(String token) {
        getViewState().showWait();

        Subscription subscription = service.getOrders(new Service.OrdersCallback() {
            @Override
            public void onSuccess(Orders orders) {
                getViewState().removeWait();

                ArrayList<OrderModel> orderModels = new ArrayList<>();

                for (Order order : orders.getOrders()) {
                    OrderModel orderModel = new OrderModel();
                    orderModel.setCost(order.getCost());
                    orderModel.setAccessCode(order.getAccessCode());
                    orderModel.setStartAt(order.getStartAt());
                    orderModel.setEndAt(order.getEndAt());
                    orderModel.setPcName(order.getPlace().getComputer().getName());

                    String date = Utils.getFormattedDate(order.getStartAt());
                    String time = formatTime(order.getStartAt()) + "-" + formatTime(order.getEndAt());
                    orderModel.setDate(date);
                    orderModel.setTime(time);
                    orderModel.setThumbnail(R.drawable.main_screen);
                    orderModels.add(orderModel);
                }

                getViewState().onGetOrdersSuccess(orderModels);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().removeWait();
                getViewState().onGetOrdersFailure(networkError.getAppErrorMessage());
            }

        },token);

        subscriptions.add(subscription);
    }



    public void onStop() {
        subscriptions.unsubscribe();
    }
}
