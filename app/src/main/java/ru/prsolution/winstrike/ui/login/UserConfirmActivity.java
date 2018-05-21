package ru.prsolution.winstrike.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.logging.ConfirmModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.mvp.presenters.UserConfirmPresenter;
import ru.prsolution.winstrike.mvp.views.UserConfirmView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.common.YandexWebView;
import rx.Observable;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.TextFormat.setTextColor;
import static ru.prsolution.winstrike.common.utils.TextFormat.simplePhoneFormat;
import static ru.prsolution.winstrike.common.utils.Utils.setBtnEnable;

/**
 * Created by designer on 15/03/2018.
 */

public class UserConfirmActivity extends AppCompatActivity implements UserConfirmView {

    @BindView(R.id.tv_hint)
    TextView codeSentLabel;

    @BindView(R.id.v_code)
    View codeTextFieldBackGround;

    @BindView(R.id.et_code)
    EditText codeTextField;

    @BindView(R.id.confirm_button)
    View confirmCodeButton;

    @BindView(R.id.et_name)
    EditText nameTextField;

    @BindView(R.id.v_name)
    View nameTextFieldBackGround;

    @BindView(R.id.v_nextbtn)
    View nextButton;

    @BindView(R.id.tv_nextbtn_label)
    TextView nextButtonLabel;

    @BindView(R.id.v_send_code_again)
    View sendCodeAgain;

    @BindView((R.id.tv_send_code_again))
    TextView sendCodeAgainLabel;


    @BindView(R.id.text_footer2)
    TextView text_footer2;

    @BindView(R.id.text_footer4)
    TextView text_footer4;


    @Inject
    public Service service;

    private UserConfirmPresenter presenter;
    private ConfirmModel user;
    private String phone;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
        presenter.onStop();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       WinstrikeApp.getInstance().getAppComponent().inject(this);

        renderView();
        init();

        presenter = new UserConfirmPresenter(service, this);
    }

    private void renderView() {
        setContentView(R.layout.ac_confsmscode);
        ButterKnife.bind(this);
    }

    private void init() {

        phone = getIntent().getStringExtra("phone");
        if (phone == null) {
            phone = "+79520757099";
        }

        confirmSuccess();

        setBtnEnable(nextButton, false);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        // Меняем видимость кнопки  корректном вводе кода из смс
        RxTextView.textChanges(codeTextField).subscribe(
                it -> {
                    Boolean fieldOk = codeTextField.getText().length() >= 6;
                    if (fieldOk) {
                        setBtnEnable(confirmCodeButton, true);
                    } else {
                        setBtnEnable(confirmCodeButton, false);
                    }
                }
        );

        // Подтверждаем пользователя
        confirmCodeButton.setOnClickListener(
                it -> {
                    String sms_code = String.valueOf(codeTextField.getText());
                    Timber.d("sms_code: %s", sms_code);

                    if (dpHeight < 600) {
                        codeTextFieldBackGround.setVisibility(View.GONE);
                        codeTextField.setVisibility(View.GONE);
                    }

                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    user = new ConfirmModel();
                    user.setPhone(phone);
                    presenter.confirmUser(sms_code, user);
                }
        );

        // Вводим имя пользователя и переходим на главный экран
        RxTextView.textChanges(nameTextField).subscribe(
                it -> {
                    Boolean fieldOk = nameTextField.getText().length() >= 4;
                    if (fieldOk) {
//                        saveUserName(nameTextField.getText().toString());
                        setBtnEnable(nextButton, true);
                        nextButtonLabel.setText("Поехали!");
                        nextButton.setOnClickListener(
                                v -> startActivity(new Intent(this, SignInActivity.class))
                        );
                    } else {
                        setBtnEnable(confirmCodeButton, false);
                        setBtnEnable(nextButton, false);
                    }
                }
        );


        setTextColor(codeSentLabel, "Введите 6-значный код, который был\n" +
                "отправлен на номер", simplePhoneFormat(phone), "#9b9b9b", "#000000");

        setFooter();

    }

    private void setFooter() {
        String mystring = new String("Условиями");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        text_footer2.setText(content);

        text_footer2.setOnClickListener(
                it -> {
                    Intent browserIntent = new Intent(this, YandexWebView.class);
                    String url = "file:///android_asset/rules.html";
                    browserIntent.putExtra("url", url);
                    startActivity(browserIntent);
                }
        );

        text_footer4.setOnClickListener(
                it -> {
                    Intent browserIntent = new Intent(this, YandexWebView.class);
                    String url = "file:///android_asset/politika.html";
                    browserIntent.putExtra("url", url);
                    startActivity(browserIntent);
                }
        );

        String textFooter = new String("Политикой конфиденциальности");
        SpannableString content4 = new SpannableString(textFooter);
        content4.setSpan(new UnderlineSpan(), 0, textFooter.length(), 0);
        text_footer4.setText(content4);
    }


    private void confirmSuccess() {
/*                    if (isVisible) {
                        nameTextFieldBackGround.setVisibility(View.VISIBLE);
                        nameTextField.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.VISIBLE);
                        nextButtonLabel.setVisibility(View.VISIBLE);
                    } else {
                        nameTextFieldBackGround.setVisibility(View.INVISIBLE);
                        nameTextField.setVisibility(View.INVISIBLE);
                        nextButton.setVisibility(View.INVISIBLE);
                        nextButtonLabel.setVisibility(View.INVISIBLE);
                    }*/
        confirmCodeButton.setVisibility(View.INVISIBLE);
        nameTextField.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        sendCodeAgain.setVisibility(View.INVISIBLE);
    }




    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    @Override
    public void onUserConfirmFailure(String appErrorMessage) {
        Timber.w("UserEntity confirm failure: %s", appErrorMessage);
        if (appErrorMessage.contains("409")) toast("Не верный код");
        if (appErrorMessage.contains("403")) toast("Пользователь уже поддвержден");
        if (appErrorMessage.contains("404"))
            toast("Ошибка регистрации! Возможно код неверен или пользователь уже существует");
        if (appErrorMessage.contains("406")) toast("Код просрочен");
    }

    @Override
    public void onUserConfirmSuccess(MessageResponse confirmModel) {
        Timber.d("UserEntity confirm successfully: %s", confirmModel.getMessage());
        toast("Пользователь подтвержден");
        setBtnEnable(confirmCodeButton, false);
        confirmSuccess();
    }


    @Override
    public void onSendSmsSuccess(MessageResponse authResponse) {
        Timber.d("Sms send successfully: %s", authResponse.getMessage());
        toast("Код выслан повторно");
    }

    @Override
    public void onSmsSendFailure(String appErrorMessage) {
        Timber.d("Sms send failure: %s", appErrorMessage);
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
