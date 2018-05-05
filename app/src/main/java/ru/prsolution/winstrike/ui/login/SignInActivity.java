package ru.prsolution.winstrike.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.HelpActivity;
import ru.prsolution.winstrike.common.logging.SignInModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.common.utils.TextFormat;
import ru.prsolution.winstrike.db.UserViewModel;
import ru.prsolution.winstrike.db.entity.UserEntity;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.presenters.SignInPresenter;
import ru.prsolution.winstrike.mvp.views.SignInView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;
import rx.Observable;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.TextFormat.formatPhone;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot1Color;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot2Color;
import static ru.prsolution.winstrike.common.utils.Utils.setBtnEnable;

/*
 * Created by oleg on 31.01.2018.
 */

public class SignInActivity extends MvpAppCompatActivity implements SignInView {
    @BindView(R.id.et_phone)
    EditText mPhoneView;
    @BindView(R.id.et_password)
    EditText mPasswordView;
    @BindView(R.id.v_button)
    View mSignInButton;
    @BindView(R.id.text_help_link)
    TextView mHelpLinkView;

    @BindView(R.id.text_footer)
    TextView mFooterAccountView;

    @BindView(R.id.text_footer2)
    TextView mFooterSingUpView;


    @Inject
    Router router;

    @Inject
    NavigatorHolder navigatorHolder;

    @Inject
    public Service mService;


    private SignInModel signInModel;
    private ProgressDialog mProgressDialog;
    private UserViewModel mUserViewModel;


    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
        mSignInPresenter.onStop();
    }

    @InjectPresenter
    SignInPresenter mSignInPresenter;

    @ProvidePresenter
    public SignInPresenter createSignInPresenter() {
        return new SignInPresenter(mService, router);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        WinstrikeApp.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);


        renderView();
        init();


        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        /**
         *  Check if user is confirmed, if true - go to MainScreenActivity
         *  no - go to sendSms.
         */
        mUserViewModel.getUser().observe(this, (userEntity ->{
            if (userEntity != null) {
                Timber.d("User load successfully: %s",userEntity.getConfirmed());
                if (userEntity.getConfirmed()) {
//                    toast("Пользователь авторизован");
                    router.replaceScreen(Screens.START_SCREEN);
                    Timber.d("Success signIn");
                } else {
                    toast("Пользователь не подтвержден");
                    ConfirmSmsModel smsModel = new ConfirmSmsModel();
                    smsModel.setUsername(userEntity.getPhone());
                    mSignInPresenter.sendSms(smsModel);
                }
            }
        }));

    }

    public void init() {
        TextFormat.formatText(mPhoneView, "(___) ___-__-__");

        setBtnEnable(mSignInButton, false);

        mSignInButton.setOnClickListener(
                it -> {
                    signInModel = new SignInModel();
                    signInModel.setUsername(formatPhone(String.valueOf(mPhoneView.getText())));
                    signInModel.setPassword(String.valueOf(mPasswordView.getText()));

                    mSignInPresenter.signIn(signInModel);
                }
        );

        checkFieldEnabled(mPhoneView, mPasswordView, mSignInButton);

        mHelpLinkView.setOnClickListener(
                it -> startActivity(new Intent(this, HelpActivity.class))
        );

        setFooter();
    }

    private void setFooter() {
        setTextFoot1Color(mFooterAccountView, "Еще нет аккаунта?", "#9b9b9b");
        setTextFoot2Color(mFooterSingUpView, " Зарегистрируйтесь", "#c9186c");
        mFooterSingUpView.setOnClickListener(
                it -> startActivity(new Intent(this, RegisterActivity.class))
        );

    }


    public void renderView() {
        setContentView(R.layout.ac_login);
        ButterKnife.bind(this);

    }

    @Override
    public void showWait() {
        showProgressDialog();
    }

    @Override
    public void removeWait() {
        hideProgressDialog();
    }


    @Override
    public void onSendSmsSuccess(MessageResponse confirmModel) {
        Timber.tag("common").d("Sms send success: %s", confirmModel.getMessage());
        toast("Код выслан повторно");
        Intent intent = new Intent(SignInActivity.this, UserConfirmActivity.class);
        intent.putExtra("phone", formatPhone(String.valueOf(mPhoneView.getText())));
        startActivity(new Intent(SignInActivity.this, UserConfirmActivity.class));
    }

    @Override
    public void onSmsSendFailure(String appErrorMessage) {
        Timber.tag("common").w("Sms send error: %s", appErrorMessage);
        if (appErrorMessage.contains("404"))
            toast("Ошибка отправки кода! Нет пользователя с таким номером");
        if (appErrorMessage.contains("409")) toast("Ошибка функции кодогенерации");
        if (appErrorMessage.contains("422")) toast("Не указан номер телефона");
    }

    @Override
    public void onAuthResponseSuccess(AuthResponse authResponse) {
        Boolean confirmed = false;
        UserEntity userEntity = new UserEntity();



        // Save user in db
        userEntity.setPublickId(authResponse.getUser().getPublicId());
        userEntity.setPhone(authResponse.getUser().getPhone());
        userEntity.setToken(authResponse.getToken());
        userEntity.setConfirmed(confirmed);
        mUserViewModel.insert(userEntity);
    }

    @Override
    public void onAuthFailure(String appErrorMessage) {
        Timber.e("Error on auth: %s", appErrorMessage);
        if (appErrorMessage.contains("403")) toast("Неправильный пароль");
        if (appErrorMessage.contains("404")) toast("Пользователь не найден");
        if (appErrorMessage.contains("502")) toast("Ошибка сервера");
        if (appErrorMessage.contains("No Internet Connection!"))
            toast("Интернет подключение не доступно!");

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Авторизация...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void checkFieldEnabled(EditText et_phone, EditText et_pass, View button) {
        Observable<TextViewTextChangeEvent> phoneObservable = RxTextView.textChangeEvents(et_phone);
        Observable<TextViewTextChangeEvent> passwordObservable = RxTextView.textChangeEvents(et_pass);
        Observable.combineLatest(phoneObservable, passwordObservable, (phoneSelected, passwordSelected) -> {
            boolean phoneCheck = phoneSelected.text().length() >= 14;
            boolean passwordCheck = passwordSelected.text().length() >= 4;
            return phoneCheck && passwordCheck;
        }).subscribe(aBoolean -> {
            if (aBoolean) {
                setBtnEnable(button, true);
            } else {
                setBtnEnable(button, false);
            }
        });
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Navigator navigator = new Navigator() {

        @Override
        public void applyCommands(Command[] commands) {
            for (Command command : commands) applyCommand(command);
        }

        private void applyCommand(Command command) {
            if (command instanceof Forward) {
                forward((Forward) command);
            } else if (command instanceof Replace) {
                replace((Replace) command);
            } else if (command instanceof Back) {
                back();
            } else if (command instanceof SystemMessage) {
//                Toast.makeText(StartActivity.this, ((SystemMessage) command).getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Cicerone", "Illegal command for this screen: " + command.getClass().getSimpleName());
            }
        }

        private void forward(Forward command) {
            switch (command.getScreenKey()) {
/*                case Screens.START_ACTIVITY_SCREEN:
                    startActivity(new Intent(StartActivity.this, StartActivity.class));
                    break;
                case Screens.MAIN_ACTIVITY_SCREEN:
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    break;
                case Screens.BOTTOM_NAVIGATION_ACTIVITY_SCREEN:
                    startActivity(new Intent(StartActivity.this, BottomNavigationActivity.class));
                    break;
                case Screens.PROFILE_SCREEN:
                    startActivity(new Intent(StartActivity.this, ProfileActivity.class));
                    break;
                default:
                    Log.e("Cicerone", "Unknown screen: " + command.getScreenKey());
                    break;*/
            }
        }

        private void replace(Replace command) {
            if (command.getScreenKey().equals(Screens.START_SCREEN)) {
                startActivity(new Intent(SignInActivity.this, MainScreenActivity.class));
            } else {
                Log.e("Cicerone", "Unknown screen: " + command.getScreenKey());
            }
        }

        private void back() {
            finish();
        }
    };


}
