package ru.prsolution.winstrike.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;
import java.util.List;

import ru.prsolution.winstrike.mvp.apimodels.OrderModel;


/**
 * Created by terrakok 26.11.16
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PlacesView extends MvpView {

    void showWait();

    void removeWait();
}
