package ru.prsolution.winstrike.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by designer on 27/02/2018.
 */

public class Utils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static String loadJSONFromAsset(InputStream is) {
        String json = null;
        try {
//            InputStream is = getActivity().getAssets().open("yourfilename.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

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

    public static String userphoneFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
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


}
