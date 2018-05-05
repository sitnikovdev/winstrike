package ru.prsolution.winstrike.ui.guides;
/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.login.SignInActivity;

public class GuideFragment extends Fragment {
    private static String ARGUMENT_GUIDE_NUMBER = "arg_page_number";
    private Integer guideNumber = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guideNumber = getArguments().getInt(ARGUMENT_GUIDE_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View viewPre1 = inflater.inflate(R.layout.fmt_pres1, null);
        View btn_nextGuide1 = viewPre1.findViewById(R.id.btn_nextGuide);
        View fl_next1 = viewPre1.findViewById(R.id.fl_next);

        View viewPre2 = inflater.inflate(R.layout.fmt_pres2, null);
        View btn_nextGuide2 = viewPre2.findViewById(R.id.btn_nextGuide1);
        View fl_prev2 = viewPre2.findViewById(R.id.fl_prev);
        View fl_next2 = viewPre2.findViewById(R.id.fl_next);

        View viewPre3 = inflater.inflate(R.layout.fmt_pres3, null);
        View btn_nextGuide3 = viewPre3.findViewById(R.id.btn_nextGuide1);
        View fl_prev3 = viewPre3.findViewById(R.id.fl_prev);
        View nextButton = viewPre3.findViewById(R.id.next_button);
        nextButton.setOnClickListener(
                it -> startActivity(new Intent(getActivity(), SignInActivity.class))
        );

        switch (guideNumber) {
            case 0: {
                btn_nextGuide1.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(1)
                );
                fl_next1.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(1)
                );
                return viewPre1;
            }
            case 1: {
                fl_prev2.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(0)
                );
                fl_next2.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(2)
                );
                btn_nextGuide2.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(2)
                );

                return viewPre2;
            }
            case 2: {
                btn_nextGuide3.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(2)
                );
                fl_prev3.setOnClickListener(
                        it -> ((GuideActivity) getActivity()).getViewPager().setCurrentItem(1)
                );

                return viewPre3;
            }
        }
        return null;
    }

    public static GuideFragment newInstance(Integer pageNumber) {
        GuideFragment fragment = new GuideFragment();
        Bundle argument = new Bundle();
        argument.putInt(ARGUMENT_GUIDE_NUMBER, pageNumber);
        fragment.setArguments(argument);
        return fragment;
    }

}
