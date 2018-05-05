package ru.prsolution.winstrike.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by terrakok 25.11.16
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface HomeView extends MvpView {
    void showWait();

    void removeWait();
}
