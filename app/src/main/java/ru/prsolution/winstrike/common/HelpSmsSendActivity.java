package ru.prsolution.winstrike.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.login.RegisterActivity;

import static ru.prsolution.winstrike.common.utils.TextFormat.formatText;
import static ru.prsolution.winstrike.common.utils.TextFormat.setTextColor;

/*
 * Created by oleg on 01.02.2018.
 */

public class HelpSmsSendActivity extends AppCompatActivity {
    @BindView(R.id.next_button_phone)
    View next_button_phone;
    @BindView(R.id.next_button_code)
    View next_button_code;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_footer)
    TextView tv_footer;
    @BindView(R.id.back_btn_tap)
    View back_btn_tap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_help_sms);
        ButterKnife.bind(this);

        next_button_phone.setAlpha(.5f);
        next_button_phone.setClickable(false);
        next_button_code.setAlpha(.5f);
        next_button_code.setClickable(false);

        next_button_phone.setOnClickListener(
                it ->
                    startActivity( new Intent(this, RegisterActivity.class))
        );
        next_button_code.setOnClickListener(
                it ->
                        startActivity( new Intent(this, RegisterActivity.class))
        );

        formatText(et_phone, "___-___-__-__");

        RxTextView.textChanges(et_phone).subscribe(
                it -> {
                    if (et_phone.getText().length() == 13) {
                        next_button_phone.setAlpha(1f);
                        next_button_phone.setClickable(true);
                    }else {
                        next_button_phone.setAlpha(.5f);
                        next_button_phone.setClickable(false);
                    }
                }
        );

        RxTextView.textChanges(et_code).subscribe(
                it -> {
                    if (et_code.getText().length() == 6) {
                        next_button_code.setAlpha(1f);
                        next_button_code.setClickable(true);
                    }else {
                        next_button_code.setAlpha(.5f);
                        next_button_code.setClickable(false);
                    }
                }
        );

        setTextColor(tv_footer, "Уже есть аккуунт?", " Войдите", "#9b9b9b", "#c9186c");


        tv_footer.setOnClickListener (
            it -> startActivity(new Intent(this, SignInActivity.class))
        );

        back_btn_tap.setOnClickListener (
            it -> startActivity(new Intent(this, HelpActivity.class))
        );

    }
}

