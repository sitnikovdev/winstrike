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
import ru.prsolution.winstrike.common.utils.TextFormat;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
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
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_pass;

    @BindView(R.id.next_button_phone)
    View next_button_phone;

    @BindView(R.id.text_footer)
    TextView text_footer;
    @BindView(R.id.text_footer2)
    TextView text_footer2;


    @Inject
    public Service service;

    private RegisterPresenter presenter;
    private LoginModel user;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WinstrikeApp.getInstance().getAppComponent().inject(this);

        renderView();

        init();

        presenter = new RegisterPresenter(service, this);

    }

    void init() {
        setBtnEnable(next_button_phone, false);

        next_button_phone.setOnClickListener(
                view -> {
                    // Создание пользователя и переход на страницу подтверждения пароля
                    user = new LoginModel();
                    user.setPhone(formatPhone(String.valueOf(et_phone.getText())));
                    user.setPassword(String.valueOf(et_pass.getText()));
                    Timber.tag("common").d("Create new user...");

                    clearUser();
                    presenter.createUser(user);
                }
        );

        checkFieldEnabled(et_phone, et_pass, next_button_phone);

        TextFormat.formatText(et_phone, "(___) ___-__-__");

        setFooter();
    }

    private void setFooter() {
        setTextFoot1Color(text_footer, "Уже есть аккаунт?", "#9b9b9b");
        setTextFoot2Color(text_footer2, "Войдите", "#c9186c");

        text_footer2.setOnClickListener(
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
    public void onRegisterSuccess(AuthResponse authResponse) {
        Timber.d("Register success: %s", authResponse);
        toast("Пользователь создан");
        setOperation();
        setConfirmed(false);

        //saveUser(authResponse);
        Timber.d("Sms send successfully: %s", authResponse.getMessage());
        Intent intent =new Intent(RegisterActivity.this, UserConfirmActivity.class);
        intent.putExtra("phone", user.getPhone());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
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
}
