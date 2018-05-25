package ru.prsolution.winstrike.ui.start;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import javax.inject.Inject;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.ui.login.model.MessageResponse;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.common.AuthUtils;
import ru.prsolution.winstrike.mvp.presenters.SplashPresenter;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.guides.GuideActivity;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.login.UserConfirmActivity;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import timber.log.Timber;


public class SplashActivity extends AppCompatActivity {
    private Intent mainIntent;
    SplashPresenter mSignInPresenter;

    @Inject
    public Service mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.ac_splash);

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setImageAssetsFolder("images/mdpi");
        animationView.setAnimation("data.json");
        animationView.setRepeatCount(0);
        animationView.setScale(1f);

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:", "start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:", "end");
                if (AuthUtils.INSTANCE.isFirstLogin()) {
                    AuthUtils.INSTANCE.setFirstLogin(false);
                    mainIntent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(mainIntent);
                } else {
                    isCheckLogin();
                }
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:", "cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:", "repeat");
            }
        });
        animationView.playAnimation();

    }

    public SplashPresenter createSplashPresenter() {
        return new SplashPresenter(mService, this);
    }


    private void isCheckLogin() {
        mSignInPresenter = createSplashPresenter();
        // If user is signOut from App:  go to SingIn screen. Else: check if user is exist on server and if Ok -- go to Main screen.
        if (AuthUtils.INSTANCE.isLogout() ) {
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        } else if (!AuthUtils.INSTANCE.getToken().isEmpty()) {
            //  Check if user exist on server!!!
            //Updated 24/05/2018:  Don't checking auth, until user is logout.
/*          signInModel = new SignInModel();
            signInModel.setUsername(AuthUtils.INSTANCE.getPhone());
            signInModel.setPassword(AuthUtils.INSTANCE.getPassword());
            mSignInPresenter.signIn(signInModel);*/
            startActivity(new Intent(this, MainScreenActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        }
    }

    public void onAuthResponseSuccess(AuthResponse authResponse) {
        Timber.d("On auth success");
        // Go to main screen
        Boolean confirmed = authResponse.getUser().getConfirmed();
        AuthUtils.INSTANCE.setName(authResponse.getUser().getName());
        AuthUtils.INSTANCE.setToken(authResponse.getToken());
        AuthUtils.INSTANCE.setPhone(authResponse.getUser().getPhone());
        AuthUtils.INSTANCE.setConfirmed(authResponse.getUser().getConfirmed());
        AuthUtils.INSTANCE.setPublicid(authResponse.getUser().getPublicId());

        if (confirmed && AuthUtils.INSTANCE.isLogout() != true) {
//                        router.replaceScreen(Screens.START_SCREEN);
            startActivity(new Intent(this, MainScreenActivity.class));
            Timber.d("Success signIn");
        } else if (!confirmed) {
            // If user not confirmed: send him sms, and go to UserConfirmActivity
            ConfirmSmsModel smsModel = new ConfirmSmsModel();
            smsModel.setUsername(authResponse.getUser().getPhone());
            mSignInPresenter.sendSms(smsModel);

            Intent intent = new Intent(this, UserConfirmActivity.class);
            intent.putExtra("phone", smsModel.getUsername());
            startActivity(intent);
        }
    }

    public void onAuthFailure(String appErrorMessage) {
        // Show failure
        Timber.e("Error on auth: %s", appErrorMessage);
        if (appErrorMessage.contains("403")) toast("Неправильный пароль");
        if (appErrorMessage.contains("404")) {
            toast("Пользователь не найден");
        }
        if (appErrorMessage.contains("502")) toast("Ошибка сервера");
        if (appErrorMessage.contains("No Internet Connection!"))
            toast("Интернет подключение не доступно!");
    }

    public void onSendSmsSuccess(MessageResponse authResponse) {
    }

    public void onSendSmsFailure(String appErrorMessage) {
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

