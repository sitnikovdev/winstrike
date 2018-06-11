package ru.prsolution.winstrike.ui.main;

import android.text.TextUtils;
import android.widget.TextView;

import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.Calendar;
import java.util.List;

import ru.prsolution.winstrike.mvp.models.TimeDataModel;
import timber.log.Timber;

 class DateListener implements OnSelectDateListener {

    @Override
    public void onSelect(List<Calendar> calendar) {
        TimeDataModel.INSTANCE.setSelectDate(calendar.get(0).getTime());
        String date = TimeDataModel.INSTANCE.getSelectDate();
        TimeDataModel.INSTANCE.getDate().set(date);
        //Update time for selected date:
        if (!TextUtils.isEmpty(TimeDataModel.INSTANCE.getStart())) {
            String timeFrom = TimeDataModel.INSTANCE.getTimeFrom();
            String timeTo = TimeDataModel.INSTANCE.getTimeTo();
            TimeDataModel.INSTANCE.setStartAt(String.valueOf(timeFrom));
            TimeDataModel.INSTANCE.setEndAt(String.valueOf(timeTo));
            timeFrom = TimeDataModel.INSTANCE.getStart();
            timeTo = TimeDataModel.INSTANCE.getEnd();
            Timber.d("New date is selected: timeFrom: %s, timeTo: %s", timeFrom, timeTo);
        }
    }
}

