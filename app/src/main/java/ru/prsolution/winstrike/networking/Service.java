package ru.prsolution.winstrike.networking;

import java.util.Map;

import ru.prsolution.winstrike.common.logging.SignInModel;
import ru.prsolution.winstrike.common.logging.ConfirmModel;
import ru.prsolution.winstrike.common.logging.LoginModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.Orders;
import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ennur on 6/25/16.
 */
public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }


    public Subscription authUser(final AuthCallback callback, SignInModel user) {

        return networkService.authUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AuthResponse>>() {
                    @Override
                    public Observable<? extends AuthResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<AuthResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(AuthResponse cityListResponse) {
                        callback.onSuccess(cityListResponse);

                    }
                });
    }

    public interface AuthCallback {
        void onSuccess(AuthResponse authResponse);

        void onError(NetworkError networkError);
    }


    public Subscription sendSms(final SmsCallback callback, ConfirmSmsModel confirmModel) {

        return networkService.sendSms(confirmModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MessageResponse>>() {
                    @Override
                    public Observable<? extends MessageResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<MessageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        callback.onSuccess(messageResponse);

                    }
                });
    }

    public interface SmsCallback {
        void onSuccess(MessageResponse messageResponse);

        void onError(NetworkError networkError);
    }

    public Subscription createUser(final RegisterCallback callback, LoginModel loginModel) {

        return networkService.createUser(loginModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AuthResponse>>() {
                    @Override
                    public Observable<? extends AuthResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<AuthResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(AuthResponse messageResponse) {
                        callback.onSuccess(messageResponse);

                    }
                });
    }

    public interface RegisterCallback {
        void onSuccess(AuthResponse messageResponse);

        void onError(NetworkError networkError);
    }


    public Subscription confirmUser(final ConfirmCallback callback, String sms_code, ConfirmModel confirmPhone) {

        return networkService.confirmUser(sms_code, confirmPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MessageResponse>>() {
                    @Override
                    public Observable<? extends MessageResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<MessageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        callback.onSuccess(messageResponse);

                    }
                });
    }

    public interface ConfirmCallback {
        void onSuccess(MessageResponse messageResponse);

        void onError(NetworkError networkError);
    }


    public Subscription getActivePid(final RoomsCallback callback) {

        return networkService.getActivePid()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Rooms>>() {
                    @Override
                    public Observable<? extends Rooms> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Rooms>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(Rooms jsonResponse) {
                        callback.onSuccess(jsonResponse);

                    }
                });
    }

    public interface RoomsCallback {
        void onSuccess(Rooms jsonResponse);

        void onError(NetworkError networkError);
    }


    public Subscription getArena(final RoomLayoutCallback callback, String activeLayoutPid) {

        return networkService.getArena(activeLayoutPid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RoomLayoutFactory>>() {
                    @Override
                    public Observable<? extends RoomLayoutFactory> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<RoomLayoutFactory>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(RoomLayoutFactory jsonResponse) {
                        callback.onSuccess(jsonResponse);
                    }
                });
    }

    public interface RoomLayoutCallback {
        void onSuccess(RoomLayoutFactory jsonResponse);

        void onError(NetworkError networkError);
    }


    public Subscription getArenaByTimeRange(final RoomLayoutByTimeCallback callback, String activeLayoutPid, Map<String,String> time) {

        return networkService.getArenaByTimeRange(activeLayoutPid,time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RoomLayoutFactory>>() {
                    @Override
                    public Observable<? extends RoomLayoutFactory> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<RoomLayoutFactory>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(RoomLayoutFactory jsonResponse) {
                        callback.onSuccess(jsonResponse);
                    }
                });
    }

    public interface RoomLayoutByTimeCallback {
        void onSuccess(RoomLayoutFactory jsonResponse);

        void onError(NetworkError networkError);
    }

    public Subscription getPayment(final PaymentCallback callback, String token, PaymentModel payModel) {

        return networkService.getPayment(token, payModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PaymentResponse>>() {
                    @Override
                    public Observable<? extends PaymentResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<PaymentResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(PaymentResponse jsonResponse) {
                        callback.onSuccess(jsonResponse);

                    }
                });
    }

    public interface PaymentCallback {
        void onSuccess(PaymentResponse jsonResponse);

        void onError(NetworkError networkError);
    }

    public Subscription getOrders(final OrdersCallback callback, String token) {

        return networkService.getOrders(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Orders>>() {
                    @Override
                    public Observable<? extends Orders> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Orders>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Orders orders) {
                        callback.onSuccess(orders);
                    }
                });
    }

    public interface OrdersCallback {
        void onSuccess(Orders orders);

        void onError(NetworkError networkError);
    }

}
