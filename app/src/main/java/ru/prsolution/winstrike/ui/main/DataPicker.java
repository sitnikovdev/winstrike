package ru.prsolution.winstrike.ui.main;

import android.content.Context;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.util.Calendar;

import ru.prsolution.winstrike.R;


public  class DataPicker extends DatePickerBuilder {

    public DataPicker(Context context, OnSelectDateListener onSelectDateListener) {
        super(context, onSelectDateListener);
        this
                .pickerType(CalendarView.ONE_DAY_PICKER)
                .headerColor(R.color.color_black)
                .pagesColor(R.color.color_black)
                .abbreviationsBarColor(R.color.color_black)
                .daysLabelsColor(R.color.color_primary)
                .abbreviationsLabelsColor(R.color.color_grey)
                .date(Calendar.getInstance())
                .todayLabelColor(R.color.color_accent);
    }

}
