package ru.prsolution.winstrike.mvp.presenters;

import android.widget.ImageView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.prsolution.winstrike.mvp.apimodels.Coors;
import ru.prsolution.winstrike.mvp.apimodels.End;
import ru.prsolution.winstrike.mvp.apimodels.Label;
import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.Place;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Seat;
import ru.prsolution.winstrike.mvp.apimodels.Start;
import ru.prsolution.winstrike.mvp.apimodels.Wall;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.prsolution.winstrike.mvp.views.MapView;
import ru.terrakok.cicerone.Router;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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
        List<Place> places;
        List<Seat> seats;
        List<Label> labels;
        List<Wall> walls;
        Start start;
        End end;

        RoomLayoutFactory roomLayoutFactory;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String arenaName;

        roomLayoutFactory = new RoomLayoutFactory();
        roomLayoutFactory.setRoomLayout(MapInfoSingleton.getInstance().getRoomLayout());
        arenaName = roomLayoutFactory.getRoomLayout().getName();

        places = roomLayoutFactory.getRoomLayout().getPlaces();
        labels = roomLayoutFactory.getRoomLayout().getLabels();

        walls = roomLayoutFactory.getRoomLayout().getWalls();
        start = walls.get(0).getStart();
        end = walls.get(0).getEnd();
        Integer wallStartLeft = start.getX(); // start screen position
        Integer wallEndLeft = end.getX();     // end screen position

        getViewState().showLabel(labels);

        // Ouputs seats:
        seats = new ArrayList<>();
        for (Place place : places) {
            Coors coors;
            coors = place.getCoors();

            String public_id = place.getPublicId();
            Integer seatXLeft = coors.getX();
            Integer seatYTop = coors.getY();
            Double seatAngle = coors.getAngle();
            Integer seatType = coors.getType();
            String seatStatus = place.getStatus();

            Timber.tag("Map-->").d("seat type: " + seatType);

            Seat seat = new Seat(public_id, seatXLeft, seatYTop, seatAngle, seatType, seatStatus);
            seats.add(seat);
        }
        getViewState().showSeat(seats);
    }

    public void onSeatClicked(Seat seat, ImageView ivSeat) {
/*        if (seat.getSeatType() == 0 || seat.getSeatType() == 1) {
            if (!seat.isSelected()) {
                getViewState().setSeatSelected(ivSeat, seat, true);
                MapInfoSingleton.getInstance().addToArray(seat.getPublic_id());
                seat.setSelected(true);
            } else {
                getViewState().setSeatSelected(ivSeat, seat, false);
                while (MapInfoSingleton.getInstance().getPidArray().remove(seat.getPublic_id())) {
                }
                seat.setSelected(false);
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
