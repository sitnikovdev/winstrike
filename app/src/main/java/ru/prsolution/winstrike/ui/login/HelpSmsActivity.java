package ru.prsolution.winstrike.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.mvp.models.MessageResponse;
import ru.prsolution.winstrike.common.utils.TextFormat;
import ru.prsolution.winstrike.databinding.AcSmshelpBinding;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.mvp.apimodels.NewPasswordModel;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.mvp.models.TimerViewModel;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot1Color;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot2Color;

/**
 * Created by designer on 15/03/2018.
 */

public class HelpSmsActivity extends AppCompatActivity implements TimerViewModel.TimeFinishListener {
    private Dialog dialog;
    private CompositeSubscription subscriptions;

    @Inject
    Service service;

    @BindView(R.id.displayWorkTimeLeft)
    AppCompatTextView displayWorkTimeLeft;

    @BindView(R.id.et_phone)
    EditText phoneNumber;

    @BindView(R.id.next_button_phone)
    View nextButtonPhone;

    @BindView(R.id.et_code)
    ShowHidePasswordEditText etCode;
    @BindView(R.id.next_button_confirm)
    View nextButtonConfirm;

    @BindView(R.id.tv_bntc)
    TextView tvConfirmText;

    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.v_pass)
    View vPass;

    @BindView(R.id.text_footer)
    TextView textFooter;

    @BindView(R.id.text_footer2)
    TextView textFooter2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    private TimerViewModel timer;


    @Override
    public void onTimeFinish() {
        setBtnEnable(nextButtonPhone, true);
        timer.stopButtonClicked();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

        timer = new TimerViewModel();
        timer.setListener(this);


        AcSmshelpBinding binding = DataBindingUtil.setContentView(this, R.layout.ac_smshelp);
        ButterKnife.bind(this);

        binding.setViewmodel(timer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(
                it -> startActivity(new Intent(this, HelpActivity.class))
        );

        tvToolbarTitle.setText(R.string.help_menu_sms);

        this.subscriptions = new CompositeSubscription();
//        apiService = ApiServiceImpl.getNewInstance(this).getApi();

        setConfirmVisible(false);

        setBtnEnable(nextButtonPhone, false);
        setBtnEnable(nextButtonConfirm, false);


        // Высылаем код
        nextButtonPhone.setOnClickListener(
                view -> {
                    // Запрос кода подтверждения повторно
                    ConfirmSmsModel auth = getConfirmSmsModel();
                    sendSms(auth);
                    timer.startButtonClicked();
                    setBtnEnable(nextButtonPhone, false);
                }
        );



        /*
         * Пользователь вводит код и нажимает кнопку "Подтвердить"
         *
         */

        nextButtonConfirm.setOnClickListener(
                it -> {
                    NewPasswordModel passw = new NewPasswordModel();
                    String phone = TextFormat.formatPhone(String.valueOf(phoneNumber.getText()));
                    String smsCode = String.valueOf(etCode.getText());
                    passw.setUsername(phone);
                    // Показываем диалог для смены пароля:
                    dlgRefreshPassword(passw, smsCode);
                }
        );

        TextFormat.formatText(phoneNumber, "(___) ___-__-__");

        // Меняем видимость кнопки вводе кода
        RxTextView.textChanges(etCode).subscribe(
                it -> {
                    Boolean fieldOk = etCode.getText().length() >= 6;
                    Boolean phoneOk = phoneNumber.getText().length() == 15;
                    if (fieldOk && phoneOk) {
                        setBtnEnable(nextButtonConfirm, true);
                    } else {
                        setBtnEnable(nextButtonConfirm, false);
                    }
                }
        );
        RxTextView.textChanges(phoneNumber).subscribe(
                it -> {
                    Boolean phoneOk = phoneNumber.getText().length() == 15;
                    if (phoneOk) {
                        setBtnEnable(nextButtonPhone, true);
                    } else {
                        setBtnEnable(nextButtonPhone, false);
                    }
                }
        );


        setTextFoot1Color(textFooter, "Уже есть аккаунт?", "#9b9b9b");
        setTextFoot2Color(textFooter2, "Войдите", "#c9186c");


        textFooter2.setOnClickListener(
                it -> startActivity(new Intent(this, SignInActivity.class))
        );
    }

    @NonNull
    private ConfirmSmsModel getConfirmSmsModel() {
        ConfirmSmsModel auth = new ConfirmSmsModel();
        String phone = TextFormat.formatPhone(String.valueOf(phoneNumber.getText()));
        auth.setUsername(phone);
        return auth;
    }

    private void setConfirmVisible(Boolean isEnabled) {
        if (isEnabled) {
            nextButtonConfirm.setVisibility(View.VISIBLE);
            tvConfirmText.setVisibility(View.VISIBLE);
            etCode.setVisibility(View.VISIBLE);
            tvCode.setVisibility(View.VISIBLE);
            vPass.setVisibility(View.VISIBLE);
        } else {
            nextButtonConfirm.setVisibility(View.GONE);
            tvConfirmText.setVisibility(View.GONE);
            etCode.setVisibility(View.GONE);
            tvCode.setVisibility(View.GONE);
            vPass.setVisibility(View.GONE);
        }
    }

    private void setBtnEnable(View v, Boolean isEnable) {
        runOnUiThread(
                () -> {
                    if (isEnable) {
                        v.setAlpha(1f);
                        v.setClickable(true);
                        displayWorkTimeLeft.setVisibility(View.INVISIBLE);
                    } else {
                        v.setAlpha(.5f);
                        v.setClickable(false);
                        displayWorkTimeLeft.setVisibility(View.VISIBLE);
                    }
                }
        );
    }


    private void toast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }


    private void dlgSendSMS() {
        dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_send_sms);

        TextView cvBtnOk = dialog.findViewById(R.id.btn_ok);

        cvBtnOk.setOnClickListener(
                it -> dialog.dismiss()
        );


        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.dimAmount = 0.0f;

        wlp.gravity = Gravity.CENTER;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setDimAmount(0.5f);
        // wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        wlp.y = 200;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }

    private void dlgRefreshPassword(NewPasswordModel passw, String smsCode) {
        dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_refresh_passw);

        View cvBtnOk = dialog.findViewById(R.id.v_button);
        EditText passOne = dialog.findViewById(R.id.et_password_first);
        EditText passTwo = dialog.findViewById(R.id.et_password_second);


        cvBtnOk.setOnClickListener(
                it -> {
                    String passTexOne = String.valueOf(passOne.getText());
                    String passTexTwo = String.valueOf(passTwo.getText());
                    // check for password correctness
                    if (!TextUtils.isEmpty(passTexOne) && !TextUtils.isEmpty(passTexTwo)) {
                        if ((passOne.getText().length() < 6) || (passTwo.getText().length() < 6)) {
                            toast("Длина пароля должна быть не менее 6 символов");
                        }
                        if (passTexOne.equals(passTexTwo)) {
                            passw.setNew_password(passTexOne);
                            refreshPassword(passw, smsCode);
                            dialog.dismiss();
                        } else {
                            toast("Пароли не совпадают");
                        }
                    } else {
                        toast("Длина пароля должна быть не менее 6 символов");
                    }
                }
        );


        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setDimAmount(0.5f);
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }


    public void sendSms(ConfirmSmsModel smsModel) {

        Subscription subscription = service.sendSmsByUserRequest(new Service.SmsCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                onSendSmsSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                onAuthFailure(networkError.getAppErrorMessage());
            }

        }, smsModel);

        subscriptions.add(subscription);
    }

    private void onAuthFailure(String appErrorMessage) {
        switch (appErrorMessage) {
            case "409":
                Timber.tag("OkHttp").d("Не верный код");
                toast("Не верный код");
                break;
            case "403":
                Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                toast("Пользователь уже поддвержден");
                setBtnEnable(nextButtonPhone, false);
                break;
            case "400":
                Timber.tag("OkHttp").d("Пользователь не найден");
                toast("Пользователь с таким номером не найден");
                break;
            case "404":
                Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                toast("Пользователь уже поддвержден");
                break;
            default:
                Timber.tag("OkHttp").d("confirm code: %s", appErrorMessage);
                toast("Пользователь не подтвержден");
                break;
        }
    }

    private void onSendSmsSuccess(MessageResponse authResponse) {
        Timber.d("SMS код выслан");
        // Show dialog, that sms succesfully send
        dlgSendSMS();
        setConfirmVisible(true);
    }


    public void refreshPassword(NewPasswordModel smsModel, String smsCode) {
//        getViewState().showWait();

        Subscription subscription = service.refreshPassword(new Service.RefressPasswordCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                onRefreshPasswordSuccess(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                onRefressPasswordFailure(networkError.getAppErrorMessage());
            }

        }, smsModel, smsCode);

        subscriptions.add(subscription);
    }

    private void onRefressPasswordFailure(String appErrorMessage) {
        switch (appErrorMessage) {
            case "409":
                Timber.tag("OkHttp").d("Не верный код");
                toast("Не верный код");
                break;
            case "403":
                Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                toast("Пользователь уже поддвержден");
                setBtnEnable(nextButtonPhone, false);
                break;
            case "400":
                Timber.tag("OkHttp").d("Пользователь не найден");
                toast("Пользователь с таким номером не найден");
                break;
            case "404":
                Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                toast("Пользователь уже поддвержден");
                break;
            default:
                Timber.tag("OkHttp").d("confirm code: %s", appErrorMessage);
                toast("Пользователь не подтвержден");
                break;
        }
    }

    private void onRefreshPasswordSuccess(MessageResponse authResponse) {
        toast("Новый пароль успешно сохранен");
        startActivity(new Intent(this, SignInActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.subscriptions.unsubscribe();
    }
}

