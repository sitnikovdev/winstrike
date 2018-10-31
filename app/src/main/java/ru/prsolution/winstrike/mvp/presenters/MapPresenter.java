package ru.prsolution.winstrike.mvp.presenters;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.common.utils.AuthUtils;
import ru.prsolution.winstrike.mvp.models.GameRoom;
import ru.prsolution.winstrike.mvp.models.TimeDataModel;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.main.MapScreenFragment;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by terrakok 26.11.16
 */

public class MapPresenter {
    private final MapScreenFragment fragment;
    private CompositeSubscription subscriptions;
    private final Service service;

    public MapPresenter(Service service, MapScreenFragment fragment) {
        this.subscriptions = new CompositeSubscription();
        this.service = service;
        this.fragment = fragment;
    }


    public void readMap() {
        RoomLayoutFactory roomLayoutFactory;

        roomLayoutFactory = new RoomLayoutFactory();
        roomLayoutFactory.setRoomLayout(WinstrikeApp.getInstance().getRoomLayout());

        // Init models:
        GameRoom room = new GameRoom(roomLayoutFactory.getRoomLayout());

        fragment.showSeat(room);
    }



    /**
     *  Yo, now we can go to yandex casa and bye some cool seats in Winstrike PC club!
     */
    public void onBookingClick() {
        PaymentModel payModel;
        payModel = new PaymentModel();

        // TODO: 12/05/2018 Replace with TimeDataModel.
        payModel.setStartAt(TimeDataModel.INSTANCE.getStart());
        payModel.setEnd_at(TimeDataModel.INSTANCE.getEnd());
        payModel.setPlacesPid(TimeDataModel.INSTANCE.getPids());

        String token = "Bearer " + AuthUtils.INSTANCE.getToken();
        getPayment(token, payModel);
        fragment.onSnackBarHide();
    }

    public void initScreen() {
        fragment.onScreenInit();
    }


    public void getPayment(String token, PaymentModel paymentModel) {
        fragment.showWait();

        Subscription subscription = service.getPayment(new Service.PaymentCallback() {
            @Override
            public void onSuccess(PaymentResponse authResponse) {
                fragment.removeWait();
                fragment.onGetPaymentResponseSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                fragment.removeWait();
                fragment.onGetPaymentFailure(networkError.getAppErrorMessage());
            }

        },token, paymentModel);

        subscriptions.add(subscription);
    }

    public void onStop() {
        this.subscriptions.unsubscribe();
    }

}
