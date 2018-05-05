package ru.prsolution.winstrike.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.login.RegisterActivity;

import static ru.prsolution.winstrike.common.utils.TextFormat.setTextColor;

/*
 * Created by oleg on 01.02.2018.
 */

public class HelpPasswordActivity extends AppCompatActivity {
    @BindView(R.id.next_button_code)
    View next_button_code;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.tv_footer)
    TextView tv_footer;
    @BindView(R.id.back_btn_tap)
    View back_btn_tap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_help_password);
        ButterKnife.bind(this);


        next_button_code.setOnClickListener (
                it -> {
                    Intent intent = new Intent(this, RegisterActivity.class);
                    startActivity(intent);
                }
        );

        setTextColor(tv_hint,"Введите пароль, для авторизации по номеру ",
                 "+7 (987) 654-32-10.", "#9b9b9b", "#000000");

        setTextColor(tv_footer, "Уже есть аккуунт?", " Войдите", "#9b9b9b", "#c9186c");

        tv_footer.setOnClickListener (
           it ->  startActivity(new Intent(this, SignInActivity.class))
        );

        back_btn_tap.setOnClickListener (
           it -> startActivity(new Intent(this, HelpActivity.class))
        );

    }
}
