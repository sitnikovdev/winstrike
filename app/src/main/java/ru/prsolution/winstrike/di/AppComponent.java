package ru.prsolution.winstrike.di;


import android.content.Context;

import ru.prsolution.winstrike.presentation.help.HelpSmsActivity;
import ru.prsolution.winstrike.presentation.login.SignInActivity;
import ru.prsolution.winstrike.presentation.login.SingUpActivity;
import ru.prsolution.winstrike.presentation.login.UserConfirmActivity;
import ru.prsolution.winstrike.presentation.main.MainScreenActivity;
import ru.prsolution.winstrike.presentation.map.MapScreenFragment;
import ru.prsolution.winstrike.presentation.payment.PayScreenFragment;
import ru.prsolution.winstrike.presentation.places.PlaceScreenFragment;
import ru.prsolution.winstrike.presentation.profile.ProfileScreenFragment;
import ru.prsolution.winstrike.presentation.setup.ChooseScreenFragment;
import ru.prsolution.winstrike.presentation.splash.SplashActivity;
import ru.prsolution.winstrike.ui.main.MainContainerFragment;

public interface AppComponent {
    Context getContext();

    void inject(SignInActivity singInActivity);

    void inject(SingUpActivity singUpActivity);

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
