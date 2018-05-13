package ru.prsolution.winstrike.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.mvp.views.ProfileView;
import ru.terrakok.cicerone.Router;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by terrakok 26.11.16
 */

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> {
    private CompositeSubscription subscriptions;
    private final Service service;
    private Router router;
    private int number;

    public ProfilePresenter(Service service, Router router, int number) {
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
        //router.exit();
        router.replaceScreen(Screens.START_SCREEN,0);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }

}
