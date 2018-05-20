package ru.prsolution.winstrike.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.db.entity.UserEntity;

/*
 * Created by oleg on 03.02.2018.
 */

public class ProfileFragment extends Fragment {
    private UserEntity user;

    @BindView(R.id.next_button)
    View saveButton;

    @BindView(R.id.fio)
    EditText etFio;

    @BindView(R.id.et_password)
    ShowHidePasswordEditText etPassword;

    private final static String TITLE = "Профиль";

    OnProfileButtonsClickListener listener;

    public interface OnProfileButtonsClickListener {
        // Update user profile data (name and password)
        void onSaveButtonClick(String name, String passw);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileButtonsClickListener) {
            listener = (OnProfileButtonsClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implements OnProfileButtonsClickListener");
        }
    }

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fmt_profile_prof, container, false);
        ButterKnife.bind(this, v);
        saveButton.setOnClickListener(
                it -> {
                    String name = String.valueOf(etFio.getText());
                    String passw = String.valueOf(etPassword.getText());
                    setBtnEnable(saveButton, false);
                    listener.onSaveButtonClick(name, passw);
                }
        );

        if (WinstrikeApp.getInstance().getUser() != null) {
            this.user = WinstrikeApp.getInstance().getUser();
            if (!TextUtils.isEmpty(user.getName())) {
                etFio.setText(user.getName());
            }
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setBtnEnable(View v, Boolean isEnable) {
        if (isEnable) {
            v.setAlpha(1f);
            v.setClickable(true);
        } else {
            v.setAlpha(.5f);
            v.setClickable(false);
        }
    }
}
