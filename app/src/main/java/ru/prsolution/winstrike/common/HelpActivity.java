package ru.prsolution.winstrike.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.common.YandexWebView;
import ru.prsolution.winstrike.ui.login.HelpSmsActivity;
import ru.prsolution.winstrike.ui.login.SignInActivity;

/*
 * Created by oleg on 01.02.2018.
 */

public class HelpActivity extends AppCompatActivity {

    @BindView(R.id.tv_sms)
    TextView cv_sms;
    @BindView(R.id.tv_help)
    TextView cv_help;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_help_centr)
    TextView helpCeter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_help);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(
                it -> startActivity(new Intent(this, SignInActivity.class))
        );


        tvToolbarTitle.setText(R.string.help_title);


        cv_sms.setOnClickListener(
                it -> startActivity(new Intent(this, HelpSmsActivity.class))
        );

        helpCeter.setOnClickListener(
                it -> {
                    String url = "https://winstrike.gg";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);

                    //startActivity(new Intent(this, HelpPasswordActivity.class));
                }
        );
    }
}
