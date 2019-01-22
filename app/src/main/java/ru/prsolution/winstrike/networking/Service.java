package ru.prsolution.winstrike.networking;

import java.util.Map;

import ru.prsolution.winstrike.datasource.model.Arenas;
import ru.prsolution.winstrike.datasource.model.AuthResponse;
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel;
import ru.prsolution.winstrike.datasource.model.NewPasswordModel;
import ru.prsolution.winstrike.datasource.model.Orders;
import ru.prsolution.winstrike.datasource.model.PaymentModel;
import ru.prsolution.winstrike.datasource.model.PaymentResponse;
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory;
import ru.prsolution.winstrike.datasource.model.Rooms;
import ru.prsolution.winstrike.domain.models.FCMModel;
import ru.prsolution.winstrike.domain.models.LoginViewModel;
import ru.prsolution.winstrike.domain.models.ConfirmModel;
import ru.prsolution.winstrike.domain.models.LoginModel;
import ru.prsolution.winstrike.domain.models.MessageResponse;
import ru.prsolution.winstrike.domain.models.ProfileModel;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Service {

  private final NetworkService networkService;

  public Service(NetworkService networkService) {
    this.networkService = networkService;
  }


  public Subscription authUser(final AuthCallback callback, LoginViewModel user) {

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


  public Subscription sendSmsByUserRequest(final SmsCallback callback, ConfirmSmsModel confirmModel) {

    return networkService.sendSmsByUserRequest(confirmModel)
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

  public Subscription refreshPassword(final RefressPasswordCallback callback, NewPasswordModel confirmModel, String smsCode) {

    return networkService.refreshPassword(confirmModel, smsCode)
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

  public interface RefressPasswordCallback {

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

  public Subscription updateUser(final ProfileCallback callback, String token, ProfileModel profileModel, String publicId) {

    return networkService.updateUser(token, profileModel, publicId)
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

  public interface ProfileCallback {

    void onSuccess(MessageResponse messageResponse);

    void onError(NetworkError networkError);
  }

  public Subscription sendToken(final FcmTokenCallback callback, String token, FCMModel fcmToken) {

    return networkService.sendTocken(token, fcmToken)
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

  public interface FcmTokenCallback {

    void onSuccess(MessageResponse messageResponse);

    void onError(NetworkError networkError);
  }


  public Subscription getRooms(final RoomsCallback callback) {

    return networkService.getRooms()
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

  public Subscription getArenas(final ArenasCallback callback) {

    return networkService.getArenas()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends Arenas>>() {
          @Override
          public Observable<? extends Arenas> call(Throwable throwable) {
            return Observable.error(throwable);
          }
        })
        .subscribe(new Subscriber<Arenas>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            callback.onError(new NetworkError(e));

          }

          @Override
          public void onNext(Arenas jsonResponse) {
            callback.onSuccess(jsonResponse);

          }
        });
  }

  public interface ArenasCallback {

    void onSuccess(Arenas jsonResponse);

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


  public Subscription getArenaByTimeRange(final RoomLayoutByTimeCallback callback, String activeLayoutPid, Map<String, String> time) {

    return networkService.getArenaByTimeRange(activeLayoutPid, time)
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
          public void onCompleted() {
          }

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
