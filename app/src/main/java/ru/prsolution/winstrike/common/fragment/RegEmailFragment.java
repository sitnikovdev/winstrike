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

/*
 * Created by oleg on 02.02.2018.
 */

public class RegEmailFragment extends Fragment {
    @BindView(R.id.et_email)
    EditText et_email;

    private  OnEmailChangedListener listener;

    public interface OnEmailChangedListener {
        void onPassChanged(CharSequence it);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmailChangedListener) {
            listener = (OnEmailChangedListener) context;
        } else {
            throw new ClassCastException(context.toString() + "must implements RegEmailFragment.OnEmailChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fmt_registration_email, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        RxTextView.textChanges(et_email).subscribe(
                it -> listener.onPassChanged(it)
        );
    }

    public static RegEmailFragment newInstance() {
        RegEmailFragment fragment = new RegEmailFragment();
        return  fragment;
    }
}
