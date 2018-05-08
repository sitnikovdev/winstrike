package ru.prsolution.winstrike.mvp.presenters;

import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.SeatApi;
import ru.prsolution.winstrike.mvp.models.GameRoom;
import ru.prsolution.winstrike.mvp.models.LabelRoom;
import ru.prsolution.winstrike.mvp.models.Seat;
import ru.prsolution.winstrike.mvp.views.MapView;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by terrakok 26.11.16
 */

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {
    private CompositeSubscription subscriptions;
    private final Service service;
    private Router router;
    private int number;
    private Boolean isSeatSelected = false;

    public MapPresenter(Service service, Router router) {
        this.subscriptions = new CompositeSubscription();
        this.service = service;
        this.router = router;
        this.number = number;
    }

    public void onForwardPressed() {
        router.navigateTo(Screens.FORWARD_SCREEN, number + 1);
    }

    public void onGithubPressed() {
        router.navigateTo(Screens.GITHUB_SCREEN);
    }

    public void onBackPressed() {
//        router.exit();
        router.replaceScreen(Screens.CHOOSE_SCREEN,0);
    }

    public void readMap() {
        List<Seat> seats;
        List<LabelRoom> labels;

        RoomLayoutFactory roomLayoutFactory;

        roomLayoutFactory = new RoomLayoutFactory();
        roomLayoutFactory.setRoomLayout(MapInfoSingleton.getInstance().getRoomLayout());

        // Init models:
        GameRoom room = new GameRoom(roomLayoutFactory.getRoomLayout());

        seats = room.getSeats();
        labels = room.getLabels();

//        getViewState().showLabel(labels);
        getViewState().showSeat(room);

    }

    public void onSeatClicked(SeatApi seatApi, ImageView ivSeat) {
/*        if (seatApi.getSeatType() == 0 || seatApi.getSeatType() == 1) {
            if (!seatApi.isSelected()) {
                getViewState().setSeatSelected(ivSeat, seatApi, true);
                MapInfoSingleton.getInstance().addToArray(seatApi.getPublic_id());
                seatApi.setSelected(true);
            } else {
                getViewState().setSeatSelected(ivSeat, seatApi, false);
                while (MapInfoSingleton.getInstance().getPidArray().remove(seatApi.getPublic_id())) {
                }
                seatApi.setSelected(false);
            }
        }*/
    }

    public void showSnackBar() {
        getViewState().onSnackBarShow();
    }

    public void onBookingClick() {
        PaymentModel payModel;
        payModel = new PaymentModel();

        payModel.setStartAt(MapInfoSingleton.getInstance().getDateFromShort());
        payModel.setEnd_at(MapInfoSingleton.getInstance().getDateToShort());
        payModel.setPlacesPid(MapInfoSingleton.getInstance().getPidArray());

        // TODO: 29/04/2018 REMOVE AFTER TEST!!!
        //String token = "Bearer " + MapInfoSingleton.getInstance().getToken();
        String token = "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwdWJsaWNfaWQiOiI2MGNjNDQxYy05ZGVmLTQxZmQtOGMzMS1mYTkzN2E4MDg1OGEiLCJleHAiOjE1MjYyMjI5ODh9.uGiKrE6P7Gvq6YK-tXNMkdt4scZH_mNQuBiUuiVTegU";
        MapInfoSingleton.getInstance().setToken(token);
        getPayment(token, payModel);
        getViewState().onSnackBarHide();
    }

    public void initScreen() {
        getViewState().onScreenInit();
    }


    public void getPayment(String token, PaymentModel paymentModel) {
        getViewState().showWait();

        Subscription subscription = service.getPayment(new Service.PaymentCallback() {
            @Override
            public void onSuccess(PaymentResponse authResponse) {
                getViewState().removeWait();
                getViewState().onGetPaymentResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                getViewState().removeWait();
                getViewState().onGetPaymentFailure(networkError.getAppErrorMessage());
            }

        },token, paymentModel);

        subscriptions.add(subscription);
    }

    public void onPaySuccess(String redirectUrl) {
        router.replaceScreen(Screens.PAY_SCREEN,redirectUrl);
    }

    public void hideSnackBar() {
        getViewState().onSnackBarHide();
    }
}
