package ru.prsolution.winstrike.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.utils.TextFormat;

/*
 * Created by oleg on 02.02.2018.
 */

public class RegPhoneFragment extends Fragment {
    @BindView(R.id.et_phone)
    EditText et_phone;
    private OnPhoneChangedListener listener;

  public  interface OnPhoneChangedListener {
        void onPhoneChanged(CharSequence it);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhoneChangedListener) {
            listener = (OnPhoneChangedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implements RegPhoneFragment.OnPhoneChangeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.ac_registration, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        TextFormat.formatText(et_phone, "(___) ___-__-__");

        RxTextView.textChanges(et_phone).subscribe(
                it -> listener.onPhoneChanged(it)
        );

    }

    public static RegPhoneFragment newInstance() {
        RegPhoneFragment fragment = new RegPhoneFragment();
        return fragment;
    }
}
