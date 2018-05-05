package ru.prsolution.winstrike.common.datetimeweels.datetimepicker.DateTimeWheel.TimeWheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import timber.log.Timber;


/**
 * PopWindow for Date Pick
 */
public class TimePickerPopWin extends PopupWindow implements OnClickListener {

    private static final int DEFAULT_MIN_HOUR = 1;
    private static final int DEFAULT_MIN_MIN = 0;
    private static final int DEFAULT_MIN_SEC = 0;
    private static final int DEFAULT_MAX_HOUR = 24;
    private static final int DEFAULT_MAX_MIN = 59;
    private static final int DEFAULT_MAX_SEC = 59;


    @BindView(R.id.tv_hour_from)
    public TextView tv_h_from;
    @BindView(R.id.tv_min_from)
    public TextView tv_m_from;
    @BindView(R.id.tv_hour_to)
    public TextView tv_h_to;
    @BindView(R.id.tv_min_to)
    public TextView tv_m_to;

    @BindView(R.id.v_tap_from)
    View vTapFrom;
    @BindView(R.id.v_tap_to)
    View vTapTo;

    TimeData timeFromData = new TimeData("00", "00");

    TimeData timeToData = new TimeData("00", "00");


    public Button cancelBtn;
    public TextView confirmBtn;
    public TextView tvNumber;
    public LoopView lvNumber;
    public LoopView hourLoopView;
    public LoopView minLoopView;
    //    public LoopView secLoopView;
//    public LoopView timeMeridiemView;
    public View pickerContainerV;
    public View contentView;//root view

    private int maxHour; // max year
    private int maxMin; // max year
    private int maxSec; // max year
    private int hourPos = 0;
    private int numberPos = 0;
    private int minPos = 0;
    private int secPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;

    List<String> hourList = new ArrayList();
    List<String> numberList = new ArrayList();
    List<String> minList = new ArrayList();

    private boolean isFromTimeSelect = false;
    private boolean isToTimeSelect = false;

    public static class Builder {

        //Required
        private Context context;
        private OnTimePickedListener listener;

        public Builder(Context context, OnTimePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        //Option
        private int minHour = DEFAULT_MIN_HOUR;
        private int maxHour = DEFAULT_MAX_HOUR;
        private int minMin = DEFAULT_MIN_MIN;
        private int maxMin = DEFAULT_MAX_MIN;
        private int minSec = DEFAULT_MIN_SEC;
        private int maxSec = DEFAULT_MAX_SEC;

        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private String timeChose = getStrTime();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder timeChose(String timeChose) {
            this.timeChose = timeChose;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         *
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public TimePickerPopWin build() {
            if (minHour > maxHour) {
                throw new IllegalArgumentException();
            }
            if (minMin > maxMin) {
                throw new IllegalArgumentException();
            }
            if (minSec > maxSec) {
                throw new IllegalArgumentException();
            }
            return new TimePickerPopWin(this);
        }
    }

    public TimePickerPopWin(Builder builder) {
        this.maxHour = builder.maxHour;
        this.maxMin = builder.maxMin;
        this.maxSec = builder.maxSec;

        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        setSelectedDate(builder.timeChose);
        initTimeDateDialog();

    }

    private OnTimePickedListener mListener;

    public class TimeData {
        String hour;
        String min;

        public TimeData(String hour, String min) {
            this.hour = hour;
            this.min = min;
        }

        @Override
        public String toString() {
            return this.hour + ":" + this.min;
        }
    }


    private void initTimeDateDialog() {

        contentView = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_time_tw, null);
        ButterKnife.bind(this, contentView);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        //setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);


        tv_h_from.setText(String.valueOf(format2LenStr(hourPos+1)));
        tv_m_from.setText(String.valueOf(format2LenStr(minPos)));

        tv_h_to.setText(String.valueOf(format2LenStr(hourPos+2)));
        tv_m_to.setText(String.valueOf(format2LenStr(minPos)));

        isFromTimeSelect = true;


        pickerContainerV = contentView.findViewById(R.id.container_picker);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);

        hourLoopView = (LoopView) contentView.findViewById(R.id.picker_hour);
        minLoopView = (LoopView) contentView.findViewById(R.id.picker_min);


        vTapFrom = contentView.findViewById(R.id.v_tap_from);
        vTapTo = contentView.findViewById(R.id.v_tap_to);

        lvNumber = contentView.findViewById(R.id.picker_number);
        tvNumber = contentView.findViewById(R.id.tv_number);

        tvNumber.setVisibility(View.INVISIBLE);
        lvNumber.setVisibility(View.INVISIBLE);

        hourLoopView.setVisibility(View.GONE);
        minLoopView.setVisibility(View.GONE);


        initPickerViews(); // init year and month loop view

        setTextSize();

/*        //From time is selected
        setFromSelected();*/

        RxView.clicks(vTapFrom).subscribe(
                it -> timeFromSetOnSelect()
        );

        RxView.clicks(vTapTo).subscribe(
                it -> timeToSetOnSelect()
        );

        //set checked listen
        hourLoopView.setListener(item -> {
            Timber.tag("time").d("hourLoopView: onItemSelect, hourPos: %s", item);
            hourPos = item;
            if (isFromTimeSelect) {
                tv_h_from.setText(format2LenStr(item + 1));
                timeFromData.hour = format2LenStr(item + 1);
            } else if (isToTimeSelect) {
                tv_h_to.setText(format2LenStr(item + 1));
                timeToData.hour = format2LenStr(item + 1);
            }
        });

        lvNumber.setListener(item -> {
            Timber.tag("time").d("lvNumber: onItemSelect, numberPos: %s", item);
            int hourFrom = Integer.parseInt(tv_h_from.getText().toString());
            int minFrom = Integer.parseInt(tv_m_from.getText().toString());
            int hourInc = item + 1;
            Date timeFrom  = MapInfoSingleton.getInstance().getDateFrom();
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeFrom);
            cal.set(Calendar.HOUR_OF_DAY, hourFrom);
            cal.set(Calendar.MINUTE, minFrom);
            cal.add(Calendar.HOUR_OF_DAY, hourInc);

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);

            tv_h_to.setText(format2LenStr(hour));
            tv_m_to.setText(format2LenStr(min));

            timeToData.hour = format2LenStr(hour);
            timeToData.min = format2LenStr(min);
        });

        minLoopView.setListener(item -> {
            minPos = item;
            if (isFromTimeSelect) {
                tv_m_from.setText(format2LenStr(item));
                timeFromData.min = format2LenStr(item);
            } else if (isToTimeSelect) {
                tv_m_to.setText(format2LenStr(item));
                timeToData.min = format2LenStr(item);
            }
        });

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

    }

    private void setTextSize() {
        cancelBtn.setText(textCancel);
        confirmBtn.setText(textConfirm);
        cancelBtn.setTextColor(colorCancel);
        confirmBtn.setTextColor(colorConfirm);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn.setTextSize(btnTextsize);
        lvNumber.setTextSize(viewTextSize);
        hourLoopView.setTextSize(viewTextSize);
        minLoopView.setTextSize(viewTextSize);
    }

    private void timeFromSetOnSelect() {
        Timber.tag("time").d("timeFromSetOnSelect is fire");
        setFromSelected();
    }


    private void timeToSetOnSelect() {
        Timber.tag("time").d("timeToSetOnSelect is fire");
        setToSelected();
    }

    private void setFromSelected() {
        tv_h_from.setTextColor(Color.parseColor("#ffc9186c"));
        tv_m_from.setTextColor(Color.parseColor("#ffc9186c"));
        tv_h_to.setTextColor(Color.parseColor("#ffffffff"));
        tv_m_to.setTextColor(Color.parseColor("#ffffffff"));

        hourLoopView.setVisibility(View.VISIBLE);
        minLoopView.setVisibility(View.VISIBLE);
        lvNumber.setVisibility(View.GONE);
        tvNumber.setVisibility(View.GONE);

        isFromTimeSelect = true;
        isToTimeSelect = false;
    }

    private void setToSelected() {
        tv_h_to.setTextColor(Color.parseColor("#ffc9186c"));
        tv_m_to.setTextColor(Color.parseColor("#ffc9186c"));
        tv_h_from.setTextColor(Color.parseColor("#ffffffff"));
        tv_m_from.setTextColor(Color.parseColor("#ffffffff"));

        hourLoopView.setVisibility(View.GONE);
        minLoopView.setVisibility(View.GONE);
        lvNumber.setVisibility(View.VISIBLE);
        tvNumber.setVisibility(View.VISIBLE);

        isToTimeSelect = true;
        isFromTimeSelect = false;
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        int hourCount = maxHour;
        int minCount = maxMin;

        for (int i = 1; i <= hourCount; i++) {
            hourList.add(format2LenStr(i));
        }

        for (int j = 0; j <= minCount; j++) {
            minList.add(format2LenStr(j));
        }

        for (int i = 1; i <= hourCount; i++) {
            numberList.add(format2LenStr(i));
        }


        hourLoopView.setArrayList((ArrayList) hourList);
        hourLoopView.setInitPosition(hourPos);

        minLoopView.setArrayList((ArrayList) minList);
        minLoopView.setInitPosition(minPos);

        lvNumber.setArrayList((ArrayList) numberList);
        lvNumber.setInitPosition(numberPos);

    }


    /**
     * set selected bonus position value when initTimeDateDialog.
     *
     * @param dateStr
     */
    public void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());

            if (milliseconds != -1) {

                calendar.setTimeInMillis(milliseconds);
                hourPos = calendar.get(Calendar.HOUR);
                minPos = calendar.get(Calendar.MINUTE);
                secPos = calendar.get(Calendar.SECOND);

                String[] date = getStrTime().split(" ");
                Log.e("time", "hour: " + date[0].substring(0, 2));
                Log.e("time", "min: " + date[0].substring(3, 5));
                String hour = date[0].substring(0, 2);
                hourPos = Integer.valueOf(hour);
            }
        }
    }

    /**
     * Show bonus picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss bonus picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

//            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {

                int hour = hourPos + 0;
                int min = minPos;

                StringBuffer sb = new StringBuffer();

                sb.append(format2LenStr(hour));
                sb.append(":");
                sb.append(format2LenStr(min));
//                sb.append(":");
//                sb.append(format2LenStr(sec));
//                sb.append(" ");
//                sb.append(merediumText);
                timeFromData.hour = tv_h_from.getText().toString();
                timeFromData.min = tv_m_from.getText().toString();
                timeToData.hour = tv_h_to.getText().toString();
                timeToData.min = tv_m_to.getText().toString();

                Log.e("time", "time from is" + timeFromData);
                Log.e("time", "time from is" + timeToData);
                mListener.onTimePickCompleted(hour, min, sb.toString(), timeFromData, timeToData);
            }

            dismissPopWin();
        }
    }


    /**
     * get long from hh:mm:ss
     *
     * @param time
     * @return
     */
    public static long getLongFromyyyyMMdd(String time) {
        SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrTime() {
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
        return dd.format(new Date());
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     *
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnTimePickedListener {

        /**
         * Listener when bonus has been checked
         *
         * @param hour
         * @param min
         * @param timeDesc HH:mm:ss
         */
        void onTimePickCompleted(int hour, int min,
                                 String timeDesc, TimeData timeFromData, TimeData timeToData);
    }
}