package ru.prsolution.winstrike.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;

import static ru.prsolution.winstrike.common.utils.TextFormat.setTextColor;

/*
 * Created by oleg on 31.01.2018.
 */
// TODO: 20/02/2018 При нажатии на кнопку Далее кнопка съежает вниз и появляется поле "Введите ваше имя" и кнопка меняется на Поехали
// https://app.avocode.com/view/2957d5bb3d994ff1bbac6baf5e07a9d8

@SuppressLint("Registered")
public class HelpUnlockActivity extends AppCompatActivity {
    @BindView(R.id.cl_root)
    ConstraintLayout cl_root;

    @BindView(R.id.tv_code)
    TextView tv_code;

    @BindView(R.id.next_button)
    View next_button;

    @BindView(R.id.text_footer)
    TextView text_footer;

    @BindView(R.id.tv_hint)
    TextView tv_hint;

    @BindView(R.id.v_name)
    View vName;

    @BindView(R.id.tv_name)
    TextView tvName;


    ConstraintSet constraintSet = new ConstraintSet();
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_next_btn)

    TextView tvNextBtn;
    @BindView(R.id.v_code)
    View vCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_help_unlock);
        ButterKnife.bind(this);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        constraintSet.clone(cl_root);

        next_button.setAlpha(.5f);
        next_button.setClickable(false);
        tvName.setVisibility(View.GONE);
        vName.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);

        next_button.setOnClickListener(
                it -> {
//                    startActivity(new Intent(this, RegistrationLastActivity.class));
/*                    constraintSet.clear(next_button.getId(),ConstraintSet.TOP);
                    constraintSet.connect(vName.getId(), ConstraintSet.BOTTOM, next_button.getId(), ConstraintSet.TOP, 32);
                    constraintSet.applyTo(cl_root);*/
                    if (dpHeight < 600) {
                        vCode.setVisibility(View.GONE);
                        tv_code.setVisibility(View.GONE);
                    }

                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    tvName.setVisibility(View.VISIBLE);
                    vName.setVisibility(View.VISIBLE);
                    etName.setVisibility(View.VISIBLE);
                    next_button.setAlpha(.5f);
                    next_button.setClickable(false);

                }
        );

        RxTextView.textChanges(tv_code).subscribe(
                it -> {
                    Boolean fieldOk = tv_code.getText().length() >= 6;
                    if (fieldOk) {
                        next_button.setAlpha(1f);
                        next_button.setClickable(true);
                    } else {
                        next_button.setAlpha(.5f);
                        next_button.setClickable(false);
                    }
                }
        );

        RxTextView.textChanges(etName).subscribe(
                it -> {
                    Boolean fieldOk = etName.getText().length() >= 6;
                    if (fieldOk) {
                        next_button.setAlpha(1f);
                        next_button.setClickable(true);
                        tvNextBtn.setText("Поехали!");
                        next_button.setOnClickListener(
                                v -> startActivity(new Intent(this, MainScreenActivity.class))
                        );
                    } else {
                        next_button.setAlpha(.5f);
                        next_button.setClickable(false);
                    }
                }
        );


        setTextColor(tv_hint, "Введите 6-значный код, который был\n" +
                "отправлен на номер", "+7 (987) 654-32-10.", "#9b9b9b", "#000000");

        setTextColor(text_footer, "Уже есть аккаунт?", " Войдите", "#9b9b9b", "#c9186c");

        text_footer.setOnClickListener(
                it -> startActivity(new Intent(this, SignInActivity.class))
        );

    }
}
