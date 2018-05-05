package ru.prsolution.winstrike.mvp.presenters;

import ru.prsolution.winstrike.common.logging.LoginModel;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.views.RegisterView;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RegisterPresenter {
    private final Service service;
    private final RegisterView view;
    private CompositeSubscription subscriptions;

    public RegisterPresenter(Service service, RegisterView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void createUser(LoginModel user) {
        view.showWait();

        Subscription subscription = service.createUser(new Service.RegisterCallback() {
            @Override
            public void onSuccess(AuthResponse authResponse) {
                view.removeWait();
                view.onRegisterSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onRegisterFailure(networkError.getAppErrorMessage());
            }

        }, user);

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }

}
