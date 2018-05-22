package ru.prsolution.winstrike.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.BaseApp;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.logging.LoginModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.common.utils.TextFormat;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.common.AuthUtils;
import ru.prsolution.winstrike.mvp.presenters.RegisterPresenter;
import ru.prsolution.winstrike.networking.Service;
import rx.Observable;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.TextFormat.formatPhone;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot1Color;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot2Color;
import static ru.prsolution.winstrike.common.utils.Utils.setBtnEnable;
import ru.prsolution.winstrike.mvp.views.RegisterView;
/*
 * Created by oleg on 31.01.2018.
 */

public class RegisterActivity extends BaseApp implements RegisterView {
    @BindView(R.id.et_phone)
    EditText phoneNumber;
    @BindView(R.id.et_password)
    EditText password;

    @BindView(R.id.next_button_phone)
    View nextButton;

    @BindView(R.id.text_footer)
    TextView footerText;
    @BindView(R.id.text_footer2)
    TextView enterLabel;


    @Inject
    public Service service;

    private RegisterPresenter presenter;
    private LoginModel user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WinstrikeApp.getInstance().getAppComponent().inject(this);

        renderView();

        init();

        presenter = new RegisterPresenter(service, this);

    }

    void init() {
        setBtnEnable(nextButton, false);

        nextButton.setOnClickListener(
                view -> {
                    // Создание пользователя и переход на страницу подтверждения пароля
                    user = new LoginModel();
                    user.setPhone(formatPhone(String.valueOf(phoneNumber.getText())));
                    user.setPassword(String.valueOf(password.getText()));
                    Timber.tag("common").d("Create new user...");

                    presenter.createUser(user);

                    // TODO: 22/05/2018 For test (Send sms already confirmed user):
/*                    ConfirmSmsModel auth = getConfirmSmsModel(user.getPhone());
                    presenter.sendSms(auth);*/
                }
        );

        checkFieldEnabled(phoneNumber, password, nextButton);

        TextFormat.formatText(phoneNumber, "(___) ___-__-__");

        setFooter();
    }

    @Override
    public void onSendSmsSuccess(MessageResponse authResponse) {
        Timber.d("Sms send successfully: %s", authResponse.getMessage());
        toast("Код выслан");
        Intent intent =new Intent(RegisterActivity.this, UserConfirmActivity.class);
        intent.putExtra("phone", user.getPhone());
        startActivity(intent);
    }

    @Override
    public void onSmsSendFailure(String appErrorMessage) {
        Timber.d("Sms send failure: %s", appErrorMessage);
    }


    private ConfirmSmsModel getConfirmSmsModel(String phone) {
        ConfirmSmsModel auth = new ConfirmSmsModel();
        auth.setUsername(phone);
        return auth;
    }



    private void setFooter() {
        setTextFoot1Color(footerText, "Уже есть аккаунт?", "#9b9b9b");
        setTextFoot2Color(enterLabel, "Войдите", "#c9186c");

        enterLabel.setOnClickListener(
                it -> startActivity(new Intent(this, SignInActivity.class))
        );
    }


    void renderView() {
        setContentView(R.layout.ac_registration);
        ButterKnife.bind(this);
    }


    @Override
    public void showWait() {
    }

    @Override
    public void removeWait() {
    }


    @Override
    /**
     *  Register new user and send him sms with confirm code.
     */
    public void onRegisterSuccess(AuthResponse authResponse) {
        Timber.d("Register success: %s", authResponse);
        toast("Пользователь создан");
//        setOperation();
//        setConfirmed(false);

        //saveUser(authResponse);
        AuthUtils.INSTANCE.setToken(authResponse.getToken());
        AuthUtils.INSTANCE.setPublicid(authResponse.getUser().getPublicId());
        AuthUtils.INSTANCE.setRegistered(true);

        Timber.d("Sms send successfully: %s", authResponse.getMessage());
        Intent intent =new Intent(RegisterActivity.this, UserConfirmActivity.class);
        intent.putExtra("phone", user.getPhone());
        startActivity(intent);
    }

    @Override
    public void onRegisterFailure(String appErrorMessage) {
        Timber.d("Register failure: %s", appErrorMessage);
        if (appErrorMessage.contains("409")) toast("Пользователь уже существует");
        if (appErrorMessage.contains("422")) toast("Пароль слишком короткий.");
        if (appErrorMessage.contains("413")) {
            Timber.w("RegisterUser: Передан не правильный формат данных JSON");
            toast("Пользователь не создан");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
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
}
