package ru.prsolution.winstrike.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import org.jetbrains.annotations.NotNull;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.YandexWebView;
import ru.prsolution.winstrike.common.utils.MapViewUtils;
import ru.prsolution.winstrike.common.utils.Utils;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.models.GameRoom;
import ru.prsolution.winstrike.mvp.models.LabelRoom;
import ru.prsolution.winstrike.mvp.models.Seat;
import ru.prsolution.winstrike.mvp.models.SeatType;
import ru.prsolution.winstrike.mvp.models.TimeDataModel;
import ru.prsolution.winstrike.mvp.models.Wall;
import ru.prsolution.winstrike.mvp.presenters.MapPresenter;
import ru.prsolution.winstrike.mvp.views.MapView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.utils.Constants;
import timber.log.Timber;


/**
 * Created by terrakok 26.11.16
 */
public class MapScreenFragment extends android.support.v4.app.Fragment implements MapView {

  @BindView(R.id.rootMap)
  RelativeLayout rootLayout;
  private Snackbar snackbar;
  private Snackbar.SnackbarLayout snackLayout;

  private static final String EXTRA_NAME = "extra_name";
  private static final String ACTIVE_ARENA = "extra_number";

  private final int RLW = RelativeLayout.LayoutParams.WRAP_CONTENT;
  private RelativeLayout.LayoutParams tvParams;
  private RelativeLayout.LayoutParams seatParams;
  private RelativeLayout.LayoutParams rootLayoutParams;
  private LinkedHashMap<Integer, String> mPickedSeatsIds = new LinkedHashMap<>();
  private Float mXScaleFactor;
  private Float mYScaleFactor;
  private Float heightDp, widthDp;
  private int selectedArena = 0;


  @Inject
  Service service;

  //    @Inject
  MapPresenter presenter;

  private GameRoom mRoom;
  private RelativeLayout.LayoutParams tvDivParam;


  public static MapScreenFragment getNewInstance(String name, int number) {
    MapScreenFragment fragment = new MapScreenFragment();
    Bundle arguments = new Bundle();
    arguments.putString(EXTRA_NAME, name);
    arguments.putInt(ACTIVE_ARENA, number);
    fragment.setArguments(arguments);
    return fragment;
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frm_map, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    snackbar.dismiss();
  }

  private void initSnackBar() {
    snackbar = Snackbar.make(rootLayout, "", Snackbar.LENGTH_INDEFINITE);
    snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
    snackbar.getView().setBackgroundResource(R.drawable.btn_bukking);
    LayoutInflater layoutInflater = this.getLayoutInflater();
    View snackView = layoutInflater.inflate(R.layout.my_snackbar, null);
    snackLayout = (Snackbar.SnackbarLayout) snackbar.getView();
    snackLayout.addView(snackView);
    snackLayout.setOnClickListener(new BookingBtnListener());
    snackbar.dismiss();
  }


  @Override
  public void onScreenInit() {
    heightDp = WinstrikeApp.getInstance().getDisplayHeightDp();
    widthDp = WinstrikeApp.getInstance().getDisplayWidhtDp();
    rootLayoutParams = new RelativeLayout.LayoutParams(RLW, RLW);
    initSnackBar();
  }


  @Override
  public void showSeat(GameRoom room) {
    this.mRoom = room;
    drawSeat(mRoom);
  }

  void drawSeat(GameRoom room) {
    Wall mWall = null;
    Float height = WinstrikeApp.getInstance().getDisplayHeightPx();
    Float width = WinstrikeApp.getInstance().getDisplayWidhtPx();

    if (room.getWalls().size() > 0) {
      mWall = room.getWalls().get(0);
      mXScaleFactor = (width / mWall.getEnd().x);
      mYScaleFactor = (height / mWall.getEnd().y);
    } else {
      mXScaleFactor = (width / 358);
      mYScaleFactor = (height / 421);
    }

    Point seatSize = new Point();

    Bitmap seatBitmap = getBitmap(getContext(), R.drawable.ic_seat_gray);

    seatSize.set(seatBitmap.getWidth(), seatBitmap.getHeight());
    Point mScreenSize = MapViewUtils.Companion.calculateScreenSize(seatSize, room.getSeats(), mXScaleFactor + 0.2f, mYScaleFactor - 1.5f);

    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootLayout.getLayoutParams();
    params.setMargins(-65, -80, 100, 80);

    // Width and Height of screen
    if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
      params.width = mScreenSize.x;
      params.height = mScreenSize.y + 250;
      mYScaleFactor = mYScaleFactor - 1.5f;
    } else if (height <= Constants.SCREEN_HEIGHT_PX_1920) {
      params.setMargins(-35, -80, 100, 80);
      params.width = mScreenSize.x;
      params.height = mScreenSize.y + 380;
      mYScaleFactor = mYScaleFactor - 2.0f;
    } else if (height <= Constants.SCREEN_HEIGHT_PX_2560) {
      params.width = mScreenSize.x;
      params.height = mScreenSize.y + 150;
      mYScaleFactor = mYScaleFactor - 3f;
    } else {
      params.width = mScreenSize.x;
      params.height = mScreenSize.y + 250;
      mYScaleFactor = mYScaleFactor - 1.5f;
    }
    rootLayout.setLayoutParams(params);

    for (Seat seat : room.getSeats()) {
      String name = seat.getName();
//      Timber.d("id: %s, y: %s",seat.getId(),seat.getDy());
      String seatNumber = Utils.parseNumber(name);
      // TODO: 07/06/2018 For test:
      Integer seatIdInt = Integer.parseInt(seat.getId().toString());
      seatNumber = seatIdInt.toString();
      int numberLenth = seatNumber.length();
      int textOffsetX = 0;
      int textOffsetY = -10;
      if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
        textOffsetY = 0;
      }

      Integer dx = 0;
      Integer dy = 0;

      // Calculate offset by x for diffrent length of numbers (1, 2 and 3)
      if (numberLenth <= 1) {
        textOffsetX = 7;
      } else if (numberLenth == 2) {
        textOffsetX = 20;
      } else if (numberLenth == 3) {
        textOffsetX = 55;
      }

      if (width <= Constants.SCREEN_WIDTH_PX_720) {
        if (numberLenth <= 1) {
          textOffsetX = 7;
        } else if (numberLenth == 2) {
          textOffsetX = 10;
        } else if (numberLenth == 3) {
          textOffsetX = 25;
        }
      }

      dx = (int) ((seat.getDx() - MapViewUtils.Companion.getSeatOffsetXArena1(seat)) * mXScaleFactor);
      dy = (int) ((seat.getDy() + MapViewUtils.Companion.getSeatOffsetYArena1(seat)) * mYScaleFactor);

      // Seats numbers:
      tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
      tvParams.leftMargin = dx + seatSize.x / 2 - textOffsetX;
      tvParams.topMargin = dy + seatSize.y - textOffsetY - 10;

      Float angle = radianToDegrees(seat);
      Integer angleInt = Math.round(angle);
      Integer angleAbs = Math.abs(angleInt);

      if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
        if (angleAbs != 90) {
          tvParams.topMargin = dy + seatSize.y - textOffsetY - 2;
        } else {
          tvParams.topMargin = dy + seatSize.y - textOffsetY + 2;
        }
      } else if (height <= Constants.SCREEN_HEIGHT_PX_1920) {
        if (angleAbs != 90) {
          tvParams.topMargin = dy + seatSize.y - textOffsetY - 10;
        } else {
          tvParams.topMargin = dy + seatSize.y - textOffsetY;
        }
      } else if (height <= Constants.SCREEN_HEIGHT_PX_2560) {
        if (angleAbs != 90) {
          tvParams.topMargin = dy + seatSize.y - textOffsetY - 10;
        } else {
          tvParams.topMargin = dy + seatSize.y - textOffsetY;
        }

      } else {
        tvParams.topMargin = dy + seatSize.y - textOffsetY;
      }

      TextView textView = new TextView(getContext());
      textView.setText(seatNumber);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        textView.setTextAppearance(R.style.StemRegular10Gray);
      } else {
        textView.setTextAppearance(getContext(), R.style.StemRegular10Gray);
      }
      textView.setLayoutParams(tvParams);
      rootLayout.addView(textView);

      ImageView ivSeat = new ImageView(getContext());
      setImage(ivSeat, seat);

      rotateSeat(seatBitmap, seat, ivSeat);

      ivSeat.setLayoutParams(seatParams);

      View.OnClickListener mSeatViewOnClickListener = new mSeatViewOnClickListener(textView, seat, ivSeat, seatBitmap, mPickedSeatsIds);
      ivSeat.setOnClickListener(mSeatViewOnClickListener);
      rootLayout.addView(ivSeat);
    }

    // Labels of Rooms
    for (
        LabelRoom label : room.getLabels()) {
      String text = label.getText();
      Integer dx;
      Integer dy;

      dx = (int) ((label.getDx() - MapViewUtils.Companion.getLabelOffsetXArena1(text)) * mXScaleFactor);
      dy = (int) ((label.getDy() + MapViewUtils.Companion.getLabelOffsetYArena1(text)) * (mYScaleFactor));

      tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
      tvParams.leftMargin = dx;
      tvParams.topMargin = dy;
      if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
        tvParams.topMargin = dy - 5;
      }
      TextView textView = new TextView(getContext());
      textView.setText(text);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        textView.setTextAppearance(R.style.StemMedium15Primary);
      } else {
        textView.setTextAppearance(getContext(), R.style.StemMedium15Primary);
      }

      textView.setLayoutParams(tvParams);
      // Add horizontal line
      if (text.equals("HP STAGE 1")) {
        dx = (int) ((label.getDx() - MapViewUtils.Companion.getLabelOffsetXArena1(text)) * mXScaleFactor);
        dy = (int) ((label.getDy() - MapViewUtils.Companion.getLabelOffsetYArena1(text)) * (mYScaleFactor));
        tvDivParam = new RelativeLayout.LayoutParams(RLW, RLW);
        tvDivParam.leftMargin = dx;
        tvDivParam.topMargin = dy;
      }

      rootLayout.addView(textView);
    }

  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    snackLayout.setOnClickListener(null);
    for (int i = 0; i < rootLayout.getChildCount(); i++) {
      View v = rootLayout.getChildAt(i);
      if (v instanceof ImageView) {
        ImageView ivSeat = (ImageView) v;
        ivSeat.setOnClickListener(null);
      }

    }
    if (this.presenter != null) {
      presenter.onStop();
      presenter = null;
    }
    if (this.service != null) {
      this.service = null;
    }
  }

  /**
   * Save selected seat's pids in db. For offline mode compatibility.
   *
   * @param id - seat id
   * @param unselect - is seat selected
   * @param publicPid - pid of selected seat
   */

  private void onSelectSeat(String id, boolean unselect, String publicPid) {

    if (!unselect) {
      mPickedSeatsIds.put(Integer.parseInt(id), publicPid);
    } else {
      mPickedSeatsIds.remove(Integer.parseInt(id));
    }

    TimeDataModel.INSTANCE.setPids(mPickedSeatsIds);

    onPickedSeatChanged();
  }

  private void onPickedSeatChanged() {
    if (!mPickedSeatsIds.isEmpty()) {
      snackbar.show();
    } else {
      snackbar.dismiss();
    }
  }

  private void animateView(ImageView seatView) {
    AlphaAnimation animation1 = new AlphaAnimation(0.5f, 1.0f);
    animation1.setDuration(100);
    animation1.setStartOffset(300);
    animation1.setFillAfter(true);
    seatView.startAnimation(animation1);
  }

  private void rotateSeat(Bitmap seatBitmap, Seat seat, ImageView ivSeat) {
    seatParams = new RelativeLayout.LayoutParams(RLW, RLW);
    seatParams.leftMargin = (int) ((seat.getDx() - MapViewUtils.Companion.getSeatOffsetXArena1(seat)) * mXScaleFactor);
    seatParams.topMargin = (int) ((seat.getDy() + MapViewUtils.Companion.getSeatOffsetYArena1(seat)) * mYScaleFactor);

    Float angle = radianToDegrees(seat);

    float pivotX = seatBitmap.getWidth() / 2f;
    float pivotY = seatBitmap.getHeight() / 2f;
    ivSeat.setPivotX(pivotX);
    ivSeat.setPivotY(pivotY);
    ivSeat.setRotation(angle);
  }

  @NonNull
  private Float radianToDegrees(Seat seat) {
    return new Float(Math.toDegrees(seat.getAngle()));
  }


  private void setImage(ImageView seatImg, Seat seat) {
    switch (seat.getType()) {
      case FREE: {
        seatImg.setBackgroundResource(R.drawable.ic_seat_gray);
        break;
      }
      case HIDDEN: {
        seatImg.setBackgroundResource(R.drawable.ic_seat_darkgray);
        break;
      }
      case SELF_BOOKING: {
        seatImg.setBackgroundResource(R.drawable.ic_seat_blue);
        break;
      }
      case BOOKING: {
        seatImg.setBackgroundResource(R.drawable.ic_seat_red);
        break;
      }
      case VIP: {
        seatImg.setBackgroundResource(R.drawable.ic_seat_yellow);
        break;
      }
    }
  }


  @Override
  public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.presenter = new MapPresenter(service, this);
    presenter.initScreen();
    presenter.readMap();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    WinstrikeApp.INSTANCE.getAppComponent().inject(this);
    super.onCreate(savedInstanceState);
    this.selectedArena = getArguments().getInt(ACTIVE_ARENA);
  }


  private class BookingBtnListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
      String timeFrom = "", timeTo = "";
      timeFrom = TimeDataModel.INSTANCE.getStart();
      timeTo = TimeDataModel.INSTANCE.getEnd();
      if (Utils.valideateDate(timeFrom, timeTo)) {
        presenter.onBookingClick();
      } else {
        toast(getActivity().getResources().getString(R.string.toast_wrong_range));
      }
    }
  }

  @Override
  public void onSnackBarShow() {
    snackbar.show();
  }

  @Override
  public void onSnackBarHide() {
    snackbar.dismiss();
  }

  @Override
  public void onStop() {
    super.onStop();
    snackbar.dismiss();

  }


  @Override
  public void showWait() {
  }

  @Override
  public void removeWait() {
  }

  @Override
  public void onGetPaymentResponseSuccess(PaymentResponse payResponse) {
    Timber.tag("common").d("Pay successfully: %s", payResponse);

    String url = payResponse.getRedirectUrl();
    Intent intent = new Intent(this.getContext(), YandexWebView.class);
    intent.putExtra("url", url);
    startActivity(intent);
  }

  /**
   * Something goes wrong, and we can't bye seat from Winstrike PC club. show user toast with description this fucking situation.
   */
  @Override
  public void onGetPaymentFailure(String appErrorMessage) {
    String timeFrom = TimeDataModel.INSTANCE.getStart();
    String timeTo = TimeDataModel.INSTANCE.getEnd();
    Timber.d("timeFrom: %s", timeFrom);
    Timber.d("timeTo: %s", timeTo);

    Timber.tag("common").w("Failure on pay: %s", appErrorMessage);
    if (appErrorMessage.contains(getString(R.string.msg_server_500))) {
      toast(getString(R.string.msg_server_internal_err));
    }
    if (appErrorMessage.contains(getString(R.string.msg_server_400))) {
      toast(getString(R.string.msg_no_data));
    }
    if (appErrorMessage.contains(getString(R.string.msg_server_401))) {
      toast(getString(R.string.msg_no_auth));
    }
    if (appErrorMessage.contains(getString(R.string.msg_serve_403))) {
      toast(getString(R.string.msg_auth_err));
    }
    if (appErrorMessage.contains(getString(R.string.msg_server_404))) {
      toast(getString(R.string.msg_no_seat_with_id));
    }
    if (appErrorMessage.contains(getString(R.string.msg_server_405))) {
      toast(getString(R.string.msg_auth_error));
    }
    if (appErrorMessage.contains(getString(R.string.msg_server_424))) {
      toast(getString(R.string.msg_date_error));
    }
    if (appErrorMessage.contains(getString(R.string.msg_server_416))) {
      toast(getString(R.string.msg_booking_error));
    } else {
      toast(getString(R.string.msg_bookin_err));
    }
  }

  protected void toast(String message) {
    if (message.contains("авторизации")) {
      Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
  }

  private Bitmap getBitmap(VectorDrawable vectorDrawable) {
    Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
        vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    vectorDrawable.draw(canvas);
    Timber.d("getBitmap: 1");
    return bitmap;
  }

  private Bitmap getBitmap(Context context, Integer drawableId) {
    Timber.d("getBitmap: 2");
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);
    Bitmap bitmap;

    if (drawable instanceof BitmapDrawable) {
      return BitmapFactory.decodeResource(context.getResources(), drawableId);
    } else if (drawable instanceof VectorDrawable) {
      bitmap = getBitmap((VectorDrawable) drawable);
    } else {
      throw new IllegalArgumentException("unsupported drawable type");
    }
    return bitmap;
  }


  private class mSeatViewOnClickListener implements View.OnClickListener {

    private final ImageView ivSeat;
    private TextView seatName;
    private Seat seat;
    private Bitmap seatBitmap;
    private LinkedHashMap mPickedSeatsIds;

    public mSeatViewOnClickListener(TextView textView, Seat seat, ImageView ivSeat, Bitmap seatBitmap, LinkedHashMap mPickedSeatsIds) {
      this.seat = seat;
      this.ivSeat = ivSeat;
      this.seatBitmap = seatBitmap;
      this.mPickedSeatsIds = mPickedSeatsIds;
      this.seatName = textView;
    }

    @Override
    public void onClick(View v) {
      if (seat.getType() == SeatType.FREE || seat.getType() == SeatType.VIP) {
        if (!mPickedSeatsIds.containsKey(Integer.parseInt(seat.getId()))) {
          ivSeat.setBackgroundResource(R.drawable.ic_seat_picked);
          onSelectSeat(seat.getId(), false, seat.getPublicPid());
          Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.getId(), seat.getType(), seat.getPcname(), seat.getPublicPid());
          seatName.setTextColor(Color.WHITE);
          seatName.setTypeface(null, Typeface.BOLD);
        } else {
          rootLayout.removeView(ivSeat);
          setImage(ivSeat, seat);
          rotateSeat(seatBitmap, seat, ivSeat);
          rootLayout.addView(ivSeat);
          onSelectSeat(seat.getId(), true, seat.getPublicPid());
          seatName.setTextColor(ContextCompat.getColor(getActivity(), R.color.label_gray));
        }
      } else {
        Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.getId(), seat.getType(), seat.getPcname(), seat.getPublicPid());
        animateView(ivSeat);
        seatName.setTypeface(null, Typeface.NORMAL);
      }
    }
  }

}
