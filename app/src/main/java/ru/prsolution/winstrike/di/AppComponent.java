package ru.prsolution.winstrike.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.prsolution.winstrike.db.UserViewModel;
import ru.prsolution.winstrike.di.module.AppRepositoryModule;
import ru.prsolution.winstrike.di.module.ContextModule;
import ru.prsolution.winstrike.di.module.LocalNavigationModule;
import ru.prsolution.winstrike.di.module.NavigationModule;
import ru.prsolution.winstrike.networking.NetworkModule;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.login.RegisterActivity;
import ru.prsolution.winstrike.ui.login.UserConfirmActivity;
import ru.prsolution.winstrike.ui.main.ChooseScreenFragment;
import ru.prsolution.winstrike.ui.main.MapScreenFragment;
import ru.prsolution.winstrike.ui.main.PayScreenFragment;
import ru.prsolution.winstrike.ui.main.PlaceScreenFragment;
import ru.prsolution.winstrike.ui.main.ProfileScreenFragment;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import ru.prsolution.winstrike.ui.main.MainContainerFragment;

/**
 * Created by ennur on 6/28/16.
 */
@Singleton
@Component(modules = {ContextModule.class, AppRepositoryModule.class, NetworkModule.class, NavigationModule.class, LocalNavigationModule.class})
public interface AppComponent {
    Context getContext();

    void inject(SignInActivity singInActivity);

    void inject(RegisterActivity registerActivity);

    void inject(UserConfirmActivity smsConfirmActivity);

    void inject(MainScreenActivity mainScreenActivity);

    void inject(MainContainerFragment fragment);

    void inject(ChooseScreenFragment fragment);

    void inject(MapScreenFragment fragment);

    void inject(PayScreenFragment fragment);

    void inject(PlaceScreenFragment fragment);

    void inject(ProfileScreenFragment fragment);

    void inject(UserViewModel userViewModel);
}
