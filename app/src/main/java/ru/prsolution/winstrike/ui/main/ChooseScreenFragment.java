package ru.prsolution.winstrike.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.datetimeweels.TimeWheel.DataPicker;
import ru.prsolution.winstrike.databinding.FrmChooseBinding;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.mvp.models.SeatModel;
import ru.prsolution.winstrike.mvp.models.TimeDataModel;
import ru.prsolution.winstrike.mvp.presenters.ChooseScreenPresenter;
import ru.prsolution.winstrike.mvp.views.IChooseView;
import ru.prsolution.winstrike.networking.Service;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.Utils.valideateDate;


public class ChooseScreenFragment extends Fragment implements IChooseView {

    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_NUMBER = "extra_number";

    public ProgressDialog mProgressDialog;
    private onMapShowProcess listener;

    private DataPicker dataPicker;
    private DateListener dateListener;
    private TimePickerDialog timePickerDialog;
    String timeFrom, timeTo;

    FrmChooseBinding binding;


    public ChooseScreenPresenter presenter;

    @Inject
    public Service service;


    /**
     * route show map to main presenter in MainScreenActivity
     */
    public interface onMapShowProcess {
        void onMapShow();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onMapShowProcess) {
            listener = (onMapShowProcess) context;
        } else {
            throw new ClassCastException(context.toString() + " must implements onMapShowProcess");
        }
    }


    public static ChooseScreenFragment getNewInstance(String name, int number) {
        ChooseScreenFragment fragment = new ChooseScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putInt(EXTRA_NUMBER, number);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        this.presenter = new ChooseScreenPresenter(service, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SeatModel seat = WinstrikeApp.getInstance().getSeat();
        binding = DataBindingUtil.inflate(inflater, R.layout.frm_choose, container, false);
        binding.setVm(seat);
        binding.setTd(TimeDataModel.INSTANCE);
        binding.setIChooseView((IChooseView) this);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setShowMapBtnEnable(binding.nextButton, true);

        initDateSelectDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.presenter == null) {
            this.presenter = new ChooseScreenPresenter(service, this);
        }
        initDateSelectDialog();
    }


    @Override
    public void onNextButtonClickListener() {
        setTime();
        if (valideateDate(timeFrom, timeTo)) {
            presenter.getActivePid();
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.toast_wrong_range), Toast.LENGTH_LONG).show();
        }
    }


    public void setTime() {
        timeFrom = TimeDataModel.INSTANCE.getStart();
        timeTo = TimeDataModel.INSTANCE.getEnd();
    }


    /**
     * First get active pid for room
     *
     * @param roomsResponse
     */
    @Override
    public void onGetActivePidResponseSuccess(Rooms roomsResponse) {
        Timber.d("Success get map data from server: %s", roomsResponse);
        /**
         *  data for active room pid successfully get from server.
         *  save pid and get map for selected time period
         */
        String activePid = roomsResponse.getRoom().getActiveLayoutPid();

        Map<String, String> time = new HashMap<>();
        time.put("start_at", timeFrom);
        time.put("end_at", timeTo);
        presenter.getArenaByTimeRange(activePid, time);
    }

    /**
     * Active pid success get, so now we  get map for seats
     *
     * @param roomLayoutFactory
     */
    @Override
    public void onGetArenaByTimeResponseSuccess(RoomLayoutFactory roomLayoutFactory) {
        Timber.d("Success get layout data from server: %s", roomLayoutFactory);
        /**
         * data for seat mapping successfully get from sever.
         * save map data in singleton and call MapScreenFragment from main presenter
         */
        WinstrikeApp.getInstance().setRoomLayout(roomLayoutFactory.getRoomLayout());
        if (WinstrikeApp.getInstance().getRoomLayout() != null) {
            listener.onMapShow();
        }
    }

    /**
     * Something go wrong with map request, show user message in toast
     *
     * @param appErrorMessage
     */
    @Override
    public void onGetAcitivePidFailure(String appErrorMessage) {
        Timber.d("Failure get map from server: %s", appErrorMessage);
        if (appErrorMessage.contains("502")) {
            toast("Невозможно получить места. Внутренняя ошибка сервера");
        } else {
            toast(appErrorMessage);
        }
        setShowMapBtnEnable(binding.nextButton, false);
    }

    /**
     * Something go wrong with map request
     *
     * @param appErrorMessage
     */
    @Override
    public void onGetArenaByTimeFailure(String appErrorMessage) {
        Timber.d("Failure get layout from server: %s", appErrorMessage);
        if (appErrorMessage.contains("416")) toast("Выбран не рабочий диапазон времени");
    }


    /**
     * Select date
     */
    private void initDateSelectDialog() {

        if (this.dataPicker == null) {
            dateListener = new DateListener();
            dataPicker = new DataPicker(getActivity(), dateListener);
        }

    }

    @Override
    public void onDateClickListener() {
        TimeDataModel.INSTANCE.setIsDateSelect(true);
        dataPicker.build().show();
    }

    /**
     * Check before time select that date is already selected.
     */
    @Override
    public void onTimeClickListener() {
        if (TimeDataModel.INSTANCE.getIsDateSelect()) {
            timePickerDialog = new TimePickerDialog(getActivity());
            setShowMapBtnEnable(binding.nextButton, true);
        } else {
            Toast.makeText(getActivity(), "Сначала выберите дату!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void setShowMapBtnEnable(View v, Boolean isEnable) {
        if (isEnable) {
            v.setAlpha(1f);
            v.setClickable(true);
        } else {
            v.setAlpha(.5f);
            v.setClickable(false);
        }
    }

    /**
     * show progress on seats loading
     */
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

    @Override
    public void showWait() {
        showProgressDialog();
    }

    @Override
    public void removeWait() {
        hideProgressDialog();
    }

    // TODO: 13/05/2018 Fix BAG with that function!!!
/*    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.listener != null) {
            this.listener = null;
        }
        if (this.service != null) {
            this.service = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
            presenter = null;
        }
        if (this.dataPicker != null) {
            this.dataPicker = null;
        }
        if (this.dateListener != null) {
            this.dateListener = null;
        }
    }


}
