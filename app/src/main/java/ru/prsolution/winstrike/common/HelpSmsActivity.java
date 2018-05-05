package ru.prsolution.winstrike.common;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.oldapi.ApiService;
import ru.prsolution.winstrike.oldapi.ApiServiceImpl;
import ru.prsolution.winstrike.oldapi.ServiceGenerator;
import ru.prsolution.winstrike.common.logging.ConfirmModel;
import ru.prsolution.winstrike.common.utils.TextFormat;
import ru.prsolution.winstrike.common.utils.TinyDB;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot1Color;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextFoot2Color;

/**
 * Created by designer on 15/03/2018.
 */

public class HelpSmsActivity extends AppCompatActivity implements ServiceGenerator.OnConnectionTimeoutListener {
    private TinyDB tinyDB;
    private ApiService apiService;
    private Dialog dialog;

    @BindView(R.id.et_phone)
    EditText etPhone;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_smshelp);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(
                it -> startActivity(new Intent(this, HelpActivity.class))
        );

        tvToolbarTitle.setText(R.string.help_menu_title);

        tinyDB = new TinyDB(this);
        apiService = ApiServiceImpl.getNewInstance(this).getApi();

        setConfirmVisible(false);

        setBtnEnable(nextButtonPhone,false);
        setBtnEnable(nextButtonConfirm,false);

        Timber.tag("OkHttp").d("UserEntity created");
        Timber.tag("OkHttp").d("UserEntity phone: %s",tinyDB.getString("phone"));
        Timber.tag("OkHttp").d("UserEntity token: %s", tinyDB.getString("token"));
        Timber.tag("OkHttp").d("UserEntity public_id: %s", tinyDB.getString(("public_id")));

        nextButtonPhone.setOnClickListener(
                view -> {
                    // Запрос кода подтверждения повторно
                    String phone = String.valueOf(etPhone.getText());
                    Timber.tag("OkHttp").e("Second request for code by phone: %s", phone);
                    Timber.tag("OkHttp").e("UserEntity confirmed: %s", tinyDB.getBoolean("confirmed"));
                    Timber.tag("OkHttp").e("UserEntity token: %s", tinyDB.getString("token"));
                    String token = tinyDB.getString("token");
                    if (!TextUtils.isEmpty(token)) {
                        if (ApiServiceImpl.isNetworkAvailable(this)) {
                            ConfirmSmsModel auth = new ConfirmSmsModel();
                            auth.setUsername(tinyDB.getString("phone"));
                            sendCode(auth);
                        } else {
                            Toast.makeText(this, "Интернет подключение не доступно.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        // Высылаем код и показываем  диалог смены пароля
        nextButtonConfirm.setOnClickListener(
                it -> {
                    String smsCode = String.valueOf(etCode.getText());
                    Timber.tag("OkHttp").e("sms_code: %s", smsCode);
//                    confirmUser(smsCode);
//                    startActivity(new Intent(this, HelpPasswordActivity.class));
                    dlgSingOut();
                }
        );

        TextFormat.formatText(etPhone, "(___) ___-__-__");

        // Меняем видимость кнопки вводе кода
        RxTextView.textChanges(etCode).subscribe(
                it -> {
                    Boolean fieldOk = etCode.getText().length() >= 6;
                    Boolean phoneOk = etPhone.getText().length() == 15;
                    if (fieldOk && phoneOk) {
                        setBtnEnable(nextButtonConfirm,true);
                    } else {
                        setBtnEnable(nextButtonConfirm,false);
                    }
                }
        );
        RxTextView.textChanges(etPhone).subscribe(
                it -> {
                    Boolean phoneOk = etPhone.getText().length() == 15;
                    if (phoneOk) {
                        setBtnEnable(nextButtonPhone,true);
                    } else {
                        setBtnEnable(nextButtonPhone,false);
                    }
                }
        );


        setTextFoot1Color(textFooter, "Уже есть аккаунт?",  "#9b9b9b" );
        setTextFoot2Color(textFooter2, "Войдите", "#c9186c");


        textFooter2.setOnClickListener(
                it -> startActivity(new Intent(this, SignInActivity.class))
        );
    }

    private void setConfirmVisible(Boolean isEnabled) {

        runOnUiThread(
                ()->{
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
        );
    }

    private void setBtnEnable(View v, Boolean isEnable) {
        runOnUiThread(
                ()->{
                    if (isEnable) {
                        v.setAlpha(1f);
                        v.setClickable(true);
                    } else {
                        v.setAlpha(.5f);
                        v.setClickable(false);
                    }
                }
        );
    }

    public void sendCode(ConfirmSmsModel auth) {
        tinyDB.putString("operation","sendsms");
        apiService.sendConfirmCode(auth)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                            Timber.tag("OkHttp").d("confirm code: %s", message.code());
                            if (message.isSuccessful()) {
                                tinyDB.putBoolean("confirmed", true);
                                Timber.tag("OkHttp").d("confirm code: %s", message.code());
                                toast("Код выслан");
                                setBtnEnable(nextButtonPhone,false);
                                setConfirmVisible(true);
//                                startActivity(new Intent(this, ChooseSeatActivity.class));
                            } else {
                                switch (message.code()) {
                                    case 409:
                                        Timber.tag("OkHttp").d("Не верный код");
                                        toast("Не верный код");
                                        break;
                                    case 403:
                                        Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                                        toast("Пользователь уже поддвержден");
                                        setBtnEnable(nextButtonPhone,false);
                                        break;
                                    case 400:
                                        Timber.tag("OkHttp").d("Пользователь не найден");
                                        toast("Пользователь с таким номером не найден");
                                        break;
                                    case 404:
                                        Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                                        toast("404 Not Found");
                                        break;
                                    default:
                                        Timber.tag("OkHttp").d("confirm code: %s", message.code());
                                        toast("Пользователь не подтвержден");
                                        break;
                                }
                            }
                        }, error -> {
                    if (error instanceof HttpException) {
                        Response response = ((HttpException) error).response();
                        Timber.tag("OkHttp").e("Error code: %s", response.code());
                    } else {
                        error.printStackTrace();
                    }
                        }
                );
    }


    public void confirmUser(String sms_code) {
        ConfirmModel confirmModel = new ConfirmModel();
        confirmModel.setPhone(tinyDB.getString("phone"));
        apiService.confirm(sms_code, confirmModel)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                            if (message.isSuccessful()) {
                                tinyDB.putBoolean("confirmed", true);
                                Timber.tag("OkHttp").d("confirm code: %s", message.code());
                                toast("Пользователь подтвержден");
                                setBtnEnable(nextButtonConfirm,false);
                                startActivity(new Intent(this, MainScreenActivity.class));
                            } else {
                                switch (message.code()) {
                                    case 409:
                                        Timber.tag("OkHttp").d("Не верный код");
                                        toast("Не верный код");
                                        break;
                                    case 403:
                                        Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                                        toast("Пользователь уже поддвержден");
                                        break;
                                    case 404:
                                        Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                                        toast("404 Not Found");
                                        break;
                                    default:
                                        Timber.tag("OkHttp").d("confirm code: %s", message.code());
                                        toast("Пользователь не подтвержден");
                                        break;
                                }
                            }
                        }, Throwable::printStackTrace
                );
    }


    private void toast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onConnectionTimeout() {
        Timber.tag("OkHttp").e("On Connection Time Out in HelpSmsActivity class");
    }

    private void dlgSingOut() {
        dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_logout);

        TextView cvBtnOk = dialog.findViewById(R.id.btn_ok);
        TextView cvCancel = dialog.findViewById(R.id.btn_cancel);

        cvCancel.setOnClickListener(
                it -> dialog.dismiss()
        );

/*        cvBtnOk.setOnClickListener(
                it -> signOut(NODELETE)
        );*/

        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        wlp.y = 200;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }


}

