package ru.prsolution.winstrike.ui.main;

import static ru.prsolution.winstrike.common.utils.Utils.toast;
import static ru.prsolution.winstrike.common.utils.Utils.valideateDate;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.datetimeweels.TimeWheel.DataPicker;
import ru.prsolution.winstrike.databinding.FrmChooseBinding;
import ru.prsolution.winstrike.mvp.apimodels.Arenas;
import ru.prsolution.winstrike.mvp.apimodels.Room;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.mvp.models.SeatModel;
import ru.prsolution.winstrike.mvp.models.TimeDataModel;
import ru.prsolution.winstrike.mvp.presenters.ChooseScreenPresenter;
import ru.prsolution.winstrike.mvp.views.IChooseView;
import ru.prsolution.winstrike.networking.Service;
import timber.log.Timber;


public class ChooseScreenFragment extends Fragment implements IChooseView {

  private static final String EXTRA_NAME = "extra_name";
  private static final String EXTRA_NUMBER = "extra_number";

  public ProgressDialog mProgressDialog;
  private onMapShowProcess listener;

  private DataPicker dataPicker;
  private DateListener dateListener;
  private TimePickerDialog timePickerDialog;

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
  public void onResume() {
    super.onResume();
    if (this.presenter == null) {
      this.presenter = new ChooseScreenPresenter(service, this);
    }

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

  @Override
  public void onTimeClickListener() {
    if (TimeDataModel.INSTANCE.getIsDateSelect()) {
      timePickerDialog = new TimePickerDialog(getActivity());
    } else {
      toast(getActivity(), getString(R.string.first_select_date));
    }
  }


  @Override
  public void onNextButtonClickListener() {
    if (valideateDate(TimeDataModel.INSTANCE.getStart(), TimeDataModel.INSTANCE.getEnd())) {
//      presenter.getActiveArenaPid();
      presenter.getActiveArena(0);
    } else {
      toast(getActivity(), getString(R.string.toast_wrong_range));
    }
  }


  @Override
  public void onGetActivePidResponseSuccess(Rooms roomsResponse) {
    Timber.d("Success get map data from server: %s", roomsResponse);
    /**
     *  data for active room pid successfully get from server.
     *  save pid and get map for selected time period
     */
//    String activePid = roomsResponse.getRoom().getActiveLayoutPid();

    Map<String, String> time = new HashMap<>();
    time.put("start_at", TimeDataModel.INSTANCE.getStart());
    time.put("end_at", TimeDataModel.INSTANCE.getEnd());
//    presenter.getArenaByTimeRange(activePid, time);
  }

  @Override
  public void onGetArenasResponseSuccess(Arenas authResponse, int selectedArena) {
    Timber.d("Success get map data from server: %s", authResponse);
    /**
     *  data for active room pid successfully get from server.
     *  save pid and get map for selected time period
     */
    List<Room> rooms = authResponse.getRooms();

    // TODO: 17/10/2018 Add parameter for arena select:
    String activePid = rooms.get(selectedArena).getRoomLayoutPid();

//    String activePid = authResponse.getRooms().getRoom_layout_pid();

    Map<String, String> time = new HashMap<>();
    time.put("start_at", TimeDataModel.INSTANCE.getStart());
    time.put("end_at", TimeDataModel.INSTANCE.getEnd());
    presenter.getArenaByTimeRange(activePid, time);
  }

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
   */
  @Override
  public void onGetAcitivePidFailure(String appErrorMessage) {
    Timber.d("Failure get map from server: %s", appErrorMessage);
    if (appErrorMessage.contains("502")) {
      toast(getActivity(), getString(R.string.server_error_502));
    } else {
      toast(getActivity(), appErrorMessage);
    }
  }

  /**
   * Something go wrong with map request
   */
  @Override
  public void onGetArenaByTimeFailure(String appErrorMessage) {
    Timber.d("Failure get layout from server: %s", appErrorMessage);
    if (appErrorMessage.contains("416")) {
      toast(getActivity(), getString(R.string.not_working_range));
    }
  }


  /**
   * show progress on seats loading
   */
  public void showProgressDialog() {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this.getActivity());
      mProgressDialog.setMessage(getString(R.string.loading_seats));
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

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (this.listener != null) {
      this.listener = null;
    }
    if (this.service != null) {
      this.service = null;
    }
    if (this.timePickerDialog != null) {
      this.timePickerDialog = null;
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
