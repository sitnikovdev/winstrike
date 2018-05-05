package ru.prsolution.winstrike.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;

/*
 * Created by oleg on 03.02.2018.
 */

public class AppFragment extends Fragment {
    @BindView(R.id.cv_recomend)
    CardView cv_recomend;
    @BindView(R.id.cv_estimate)
    CardView cvEstimate;

    @BindView(R.id.v_vk)
    View ivVk;

    @BindView(R.id.v_instagram)
    View ivInstagram;

    @BindView(R.id.v_tweeter)
    View ivTweeter;

    @BindView(R.id.v_facebook)
    View ivFacebook;

    @BindView(R.id.v_twitch)
    View ivTwitch;

    private OnAppButtonsClickListener listener;
    private final static String TITLE = "Приложение";

    public interface OnAppButtonsClickListener {
        void onRecommendButtonClick();
        void onGooglePlayButtonClick();

        void onVkClick();

        void onInstagramClick();

        void onTweeterClick();

        void onFacebookClick();

        void onTwitchClick();
    }
    public AppFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppButtonsClickListener) {
            listener = (OnAppButtonsClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implements OnAppButtonsClickListener");
        }
    }

    public static AppFragment newInstance() {
        return new AppFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fmt_profile_app, container, false);
        ButterKnife.bind(this, v);

        cv_recomend.setOnClickListener(
               it -> listener.onRecommendButtonClick()
        );
        cvEstimate.setOnClickListener(
                it -> listener.onGooglePlayButtonClick()
        );

        ivVk.setOnClickListener(
                it->listener.onVkClick()
        );

        ivInstagram.setOnClickListener(
                it->listener.onInstagramClick()
        );

        ivTweeter.setOnClickListener(
                it->listener.onTweeterClick()
        );

        ivFacebook.setOnClickListener(
                it->listener.onFacebookClick()
        );

        ivTwitch.setOnClickListener(
                it->listener.onTwitchClick()
        );

        return v;
    }


}
