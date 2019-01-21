package ru.prsolution.winstrike.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.prsolution.winstrike.di.module.ContextModule;
import ru.prsolution.winstrike.di.module.LocalNavigationModule;
import ru.prsolution.winstrike.di.module.NavigationModule;
import ru.prsolution.winstrike.di.module.NetworkModule;
import ru.prsolution.winstrike.ui.login.HelpSmsActivity;
import ru.prsolution.winstrike.ui.login.RegisterActivity;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.login.UserConfirmActivity;
import ru.prsolution.winstrike.ui.main.ChooseScreenFragment;
import ru.prsolution.winstrike.ui.main.MainContainerFragment;
import ru.prsolution.winstrike.presentation.main.MainScreenActivity;
import ru.prsolution.winstrike.ui.main.MapScreenFragment;
import ru.prsolution.winstrike.ui.main.PayScreenFragment;
import ru.prsolution.winstrike.ui.main.PlaceScreenFragment;
import ru.prsolution.winstrike.ui.main.ProfileScreenFragment;
import ru.prsolution.winstrike.presentation.splash.SplashActivity;

@Singleton
@Component(modules = {ContextModule.class, NetworkModule.class,
        NavigationModule.class, LocalNavigationModule.class })
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

    void inject(HelpSmsActivity activity);

    void inject(SplashActivity activity);
}
