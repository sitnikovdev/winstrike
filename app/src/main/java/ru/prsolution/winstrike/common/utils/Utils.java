package ru.prsolution.winstrike.common.utils;

import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.prsolution.winstrike.mvp.transform.DateTransform;

public class Utils {

    public static String getFormattedDate(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00");
        Date date1 = null;
        try {
            date1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateformate = new SimpleDateFormat("dd MMM yyyy");
        String currentDateStr = dateformate.format(date1);
        return currentDateStr;
    }

    public static String formatTime(String start_at) {
        String[] start = start_at.split("T");
        String[] startTmp = start[1].split("\\+");
        String startTime = startTmp[0];
        String[] startTm = startTime.split(":");
        String hours = startTm[0];
        String min = startTm[1];
        String stTime = hours + ":" + min;
        return stTime;
    }


    public static void setBtnEnable(View v, Boolean isEnable) {
        if (isEnable) {
            v.setAlpha(1f);
            v.setClickable(true);
        } else {
            v.setAlpha(.5f);
            v.setClickable(false);
        }

    }


    public static boolean valideateDate(String stDate, String edDate) {
        if (stDate.isEmpty() || edDate.isEmpty()) {
            return false;
        }
        Date current = new Date();
        Date startDate = DateTransform.Companion.getDateInUTC(stDate);
        Date endDate = DateTransform.Companion.getDateInUTC(edDate);
        Boolean isDateValid = startDate.before(endDate) && (startDate.after(current) || startDate.equals(current));
        return isDateValid;
    }

}
