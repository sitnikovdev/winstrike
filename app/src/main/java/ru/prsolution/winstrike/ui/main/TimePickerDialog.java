package ru.prsolution.winstrike.ui.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.prsolution.winstrike.common.datetimeweels.TimeWheel.TimePickerPopWin;
import ru.prsolution.winstrike.mvp.models.TimeDataModel;

/**
 * Show time picker dialog.
 */
public class TimePickerDialog {
    TextView tvTime;

    public TimePickerDialog(TextView tv, Activity context) {
        this.tvTime = tv;
        int bntTextSize = 20;
        int viewTextSize = 25;
        TimePickerPopWin pickerPopWin = new TimePickerPopWin.Builder(context, (hour, min, timeDesc, timeFromData, timeToData) -> {

            String time = timeFromData + " - " + timeToData;
            tvTime.setText(time);


            TimeDataModel.INSTANCE.setTimeFrom(String.valueOf(timeFromData));
            TimeDataModel.INSTANCE.setTimeTo(String.valueOf(timeToData));


            TimeDataModel.INSTANCE.setStartAt(String.valueOf(timeFromData));
            TimeDataModel.INSTANCE.setEndAt(String.valueOf(timeToData));

            /**
             *  Save date data from timepicker (start and end).
             */
            TimeDataModel.INSTANCE.setTime(time);


        }).textConfirm("Продолжить") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(bntTextSize) // button text size
                .viewTextSize(viewTextSize) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#A9A9A9"))//color of confirm button
                .build();


        pickerPopWin.showPopWin(context);
    }

}

