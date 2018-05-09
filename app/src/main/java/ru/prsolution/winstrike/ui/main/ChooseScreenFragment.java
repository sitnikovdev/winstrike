package ru.prsolution.winstrike.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding.view.RxView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.datetimeweels.datetimepicker.DateTimeWheel.TimeWheel.TimePickerPopWin;
import ru.prsolution.winstrike.common.entity.SeatModel;
import ru.prsolution.winstrike.common.utils.TinyDB;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.mvp.presenters.ChoosePresenter;
import ru.prsolution.winstrike.mvp.views.ChooseView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.common.BackButtonListener;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.prsolution.winstrike.ui.common.RouterProvider;
import timber.log.Timber;


/**
 * Created by terrakok 26.11.16
 */
public class ChooseScreenFragment extends MvpAppCompatFragment implements ChooseView, BackButtonListener {

    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_NUMBER = "extra_number";
    private SharedPreferences sharedPref;


    public interface onMapShowClicked {
        void onMapShowClick();
    }


    onMapShowClicked listener;

    @BindView(R.id.seat_title)
    TextView seat_title;

    @BindView(R.id.head_image)
    ImageView ivSeatImg;

    @BindView(R.id.tv_date_news)
    TextView tv_date;

    @BindView(R.id.v_date_tap)
    View vDateTap;

    @BindView(R.id.v_time_tap)
    View vTimeTap;

    @BindView(R.id.next_button)
    View next_button;

    @BindView(R.id.tv_time_left)
    TextView tv_time;

    @BindView(R.id.iv_arr_time)
    ImageView iv_arr_time;

    @BindView(R.id.iv_arr_date)
    ImageView iv_arr_date;

    @BindView(R.id.cpu)
    TextView tvCpu;

    @BindView(R.id.ram)
    TextView tvRam;

    @BindView(R.id.gpu)
    TextView tvGpu;

    @BindView(R.id.monitor)
    TextView tvMonitor;

    public ProgressDialog mProgressDialog;
    private TinyDB tinyDB;
    private Calendar cal;
    private Boolean isDataSelected;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");


    private String selectedDate;
    private Boolean isDateValid;

    private Date timeFromUTC;
    private Date timeToUTC;


    @InjectPresenter
    public ChoosePresenter presenter;

    @Inject
    public Service service;

    @ProvidePresenter
    ChoosePresenter provideMainScreenPresenter() {
        return new ChoosePresenter(service,
                ((RouterProvider) getParentFragment()).getRouter()
        );
    }

    public static ChooseScreenFragment getNewInstance(String name, int number) {
        ChooseScreenFragment fragment = new ChooseScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putInt(EXTRA_NUMBER, number);
        fragment.setArguments(arguments);
        return fragment;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getActivity());
            mProgressDialog.setMessage("Загрузка мест...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private float dpHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_choose, container, false);
        ButterKnife.bind(this, view);

        SeatModel seat = MapInfoSingleton.getInstance().getSeat();


        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        Timber.tag("map").d("DpHeight: %s", dpHeight);



        if (seat != null && seat.getType().contains("VIP")) {
            ivSeatImg.setImageResource(R.drawable.vip);
            seat_title.setText("Вы выбрали: VIP место");

            tvCpu.setText("CPU: intel i7-8700k");
            tvRam.setText("RAM: 64GB, SSD HDD 500gb");
            tvGpu.setText("GPU: Geforce GTX 1080ti 11GB");
            tvMonitor.setText("Монитор: LG 32GK850G");

        } else {
            ivSeatImg.setImageResource(R.drawable.simple);
            seat_title.setText("Вы выбрали: Обычное место");

            tvCpu.setText("CPU: intel i5-8400k");
            tvRam.setText("RAM: 64GB, SSD HDD 500gb");
            tvGpu.setText("GPU: Geforce GTX 1070ti 8GB");
            tvMonitor.setText("Монитор: LG 24GM79G");
        }
        return view;
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    private Date getFormattedDateToUTCString(String dateInStr, String time) {
        Date fmtDate = new Date();

        dateInStr += 'T' + time + ":00.000000+0300";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy'T'HH:mm:ss.SSSSSSZ");
        try {
            fmtDate = formatter.parse(dateInStr);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return fmtDate;
    }

    private String getFormattedDateShortToUTCString(String dateInStr, String time) {
        Date fmtDate = new Date();
        String date;

        dateInStr += 'T' + time + ":00";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy'T'HH:mm:ss");
        try {
            fmtDate = formatter.parse(dateInStr);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(fmtDate);
        return date;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tinyDB = new TinyDB(getContext());
        isDataSelected = false;
        setBtnEnable(next_button, false);

        dateSelect();

        timeSelect();

        showMap();

    }



    private void showMap() {

        // TODO: 07/05/2018 REMOVE IT BLOCK AFTER TEST!!!
        String timeFromData = sharedPref.getString(getString(R.string.saved_time_from),"2018-05-09T18:07:00");
        String timeToData = sharedPref.getString(getString(R.string.saved_time_from),"2018-05-09T17:07:00");
        String  selectedDate = sharedPref.getString(getString(R.string.saved_date),"");
/*        String timeFromData = "2018-05-09T17:07:00";
        String timeToData = "2018-05-09T18:07:00";*/

        timeFromUTC = getFormattedDateToUTCString(selectedDate, String.valueOf(timeFromData));
        timeToUTC = getFormattedDateToUTCString(selectedDate, String.valueOf(timeToData));

        MapInfoSingleton.getInstance().setDateFrom(timeFromUTC);
        MapInfoSingleton.getInstance().setDateTo(timeToUTC);
        // TODO: 27/04/2018 Call getActivePid api mService

        presenter.getActivePid();
        // TODO: 07/05/2018 END BLOCK

        RxView.clicks(next_button).subscribe(
                it -> {
                    if (timeFromUTC != null) {
                        isDateValid = (timeFromUTC.compareTo(timeToUTC) < 0) &&
                                (timeToUTC.compareTo(new Date()) >= 0);

                        if (isDateValid) {

                            MapInfoSingleton.getInstance().setDateFrom(timeFromUTC);
                            MapInfoSingleton.getInstance().setDateTo(timeToUTC);
                            // TODO: 27/04/2018 Call getActivePid api mService


                            presenter.getActivePid();
//                            listener.onMapShowClick();
                        } else {
                            Toast.makeText(getActivity(), "Не правильно выбрана дата", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onMapShowClicked) {
            listener = (onMapShowClicked) context;
        } else {
            throw new ClassCastException(context.toString() + " must implements onMapShowClicked");
        }
    }

    private void setBtnEnable(View v, Boolean isEnable) {
        if (isEnable) {
            v.setAlpha(1f);
            v.setClickable(true);
        } else {
            v.setAlpha(.5f);
            v.setClickable(false);
        }
    }


    private void timeSelect() {
        RxView.clicks(vTimeTap).subscribe(
                it -> {
                    if (isDataSelected) {
                        openTimePickerDialog();
                    } else {
                        Toast.makeText(getActivity(), "Сначала выберите дату!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // TODO: 10/04/2018  Time and date value in dialogs must be saved.
    private void openTimePickerDialog() {
        TimePickerPopWin pickerPopWin = new TimePickerPopWin.Builder(getActivity(), (hour, min, timeDesc, timeFromData, timeToData) -> {

            String time = timeFromData + " - " + timeToData;
            tinyDB.putString("time", time);

/*
            tinyDB.putString("timeFrom", String.valueOf(timeFromData));
            tinyDB.putString("timeTo", String.valueOf(timeToData));
*/
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_time_from), String.valueOf(timeFromData));
            editor.putString(getString(R.string.saved_time_to), String.valueOf(timeToData));
            editor.putString(getString(R.string.saved_date), String.valueOf(selectedDate));

            editor.commit();


            timeFromUTC = getFormattedDateToUTCString(selectedDate, String.valueOf(timeFromData));
            timeToUTC = getFormattedDateToUTCString(selectedDate, String.valueOf(timeToData));

            MapInfoSingleton.getInstance().setDateFromShort(getFormattedDateShortToUTCString(selectedDate, String.valueOf(timeFromData)));
            MapInfoSingleton.getInstance().setDateToShort(getFormattedDateShortToUTCString(selectedDate, String.valueOf(timeToData)));


            tinyDB.putString("timeFrom", MapInfoSingleton.getInstance().getDateFromShort());
            tinyDB.putString("timeTo", MapInfoSingleton.getInstance().getDateToShort());

            int bntTextSize = 20;
            int viewTextSize = 50;

            if (dpHeight > 700) {
                bntTextSize = 20;
                viewTextSize = 50;
            }
            if (dpHeight < 700) {
                bntTextSize = 10;
                viewTextSize = 30;
            }

            tv_time.setText(time);
            Timber.tag("time").w("Time from: %s", timeFromData);
            Timber.tag("time").w("Time to: %s", timeToData);
        }).textConfirm("Продолжить") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(20) // button text size
                .viewTextSize(50) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#ffffffff"))//color of confirm button
                .build();

        setBtnEnable(next_button, true);

        pickerPopWin.showPopWin(getActivity());
    }


    private void dateSelect() {
        String date = "Выберите дату";
        String time = "00:00 - 00:00";

        if (!date.isEmpty()) {
            tv_date.setText(date);
        }
        if (!time.isEmpty()) {
            tv_time.setText(time);
        }

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        cal = Calendar.getInstance(timeZone);
        simpleDateFormat.setTimeZone(timeZone);


        OnSelectDateListener listener = calendar -> {
            selectedDate = getFormattedDate(calendar.get(0).getTime());
            cal.setTime(calendar.get(0).getTime());

/*            tinyDB.putString("rawDate", String.valueOf(calendar.get(0).getTime()));
            tinyDB.putString("date", selectedDate);*/

            MapInfoSingleton.getInstance().setSelectedDate(selectedDate);
            sharedPref.edit().putString(String.valueOf(R.string.saved_date), MapInfoSingleton.getInstance().getSelectedDate());

            tv_date.setText(selectedDate);
        };


        DatePickerBuilder builder = new DatePickerBuilder(getActivity(), listener)
                .pickerType(CalendarView.ONE_DAY_PICKER)
                .headerColor(R.color.color_black)
                .pagesColor(R.color.color_black)
                .abbreviationsBarColor(R.color.color_black)
                .daysLabelsColor(R.color.color_primary)
                .abbreviationsLabelsColor(R.color.color_grey)
                .date(Calendar.getInstance())
                .todayLabelColor(R.color.color_accent);

        DatePicker datePicker = builder.build();

        RxView.clicks(vDateTap).subscribe(
                it -> {
                    isDataSelected = true;
                    datePicker.show();
                }
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
       // startActivity(new Intent(getActivity(), MainScreenActivity.class));
        return true;
    }


    @Override
    public void setChainText(String chainText) {

    }

    @Override
    public void onDateSelect() {

    }

    @Override
    public void onTimeSelect() {

    }

    @Override
    public void onNextButtonClick() {
    }


    @Override
    public void showWait() {
        showProgressDialog();
    }

    @Override
    public void removeWait() {
        hideProgressDialog();
    }

    @Override
    public void onGetActivePidResponseSuccess(Rooms roomsResponse) {
        Timber.d("Success get map data from server: %s", roomsResponse);
        // TODO: 27/04/2018 Get room_layouts by active_layout_pid
        String activePid = roomsResponse.getRoom().getActiveLayoutPid();
        Map<String, String> time = new HashMap<>();
        String timeFrom = tinyDB.getString("timeFrom");
        String timeTo = tinyDB.getString("timeTo");

        // TODO: 06/05/2018 REMOVE AFTE TEST!!!
/*        timeFrom = "2018-05-07T14:48:00";
        timeTo = "2018-05-07T16:48:00";*/


        time.put("start_at", timeFrom);
        time.put("end_at", timeTo);
//        presenter.getArena(activePid);
        presenter.getArenaByTimeRange(activePid, time);
    }

    @Override
    public void onGetAcitivePidFailure(String appErrorMessage) {
        Timber.d("Failure get map from server: %s", appErrorMessage);
    }

    @Override
    public void onGetArenaResponseSuccess(RoomLayoutFactory roomLayoutFactory) {
        Timber.d("Success get layout data from server: %s", roomLayoutFactory);
    }

    @Override
    public void onGetArenaFailure(String appErrorMessage) {
        Timber.d("Failure get layout from server: %s", appErrorMessage);
    }

    @Override
    public void onGetArenaByTimeResponseSuccess(RoomLayoutFactory roomLayoutFactory) {
        Timber.d("Success get layout data from server: %s", roomLayoutFactory);
        MapInfoSingleton.getInstance().setRoomLayout(roomLayoutFactory.getRoomLayout());
        if (MapInfoSingleton.getInstance().getRoomLayout() != null) {
            listener.onMapShowClick();
        }
    }

    @Override
    public void onGetArenaByTimeFailure(String appErrorMessage) {
        Timber.d("Failure get layout from server: %s", appErrorMessage);
        if (appErrorMessage.contains("416")) toast("Выбран не рабочий диапазон времени");
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
