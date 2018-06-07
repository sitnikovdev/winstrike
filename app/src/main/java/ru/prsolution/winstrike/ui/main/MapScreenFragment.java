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
    private static final String EXTRA_NUMBER = "extra_number";

    private final int RLW = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private RelativeLayout.LayoutParams tvParams;
    private RelativeLayout.LayoutParams seatParams;
    private RelativeLayout.LayoutParams rootLayoutParams;
    private LinkedHashMap<Integer, String> mPickedSeatsIds = new LinkedHashMap<>();
    private Float mXScaleFactor;
    private Float mYScaleFactor;
    private Float heightDp, widthDp;


    @Inject
    Service service;

    //    @Inject
    MapPresenter presenter;

    private GameRoom mRoom;
    private RelativeLayout.LayoutParams tvDivParam;

/*    @ProvidePresenter
    MapPresenter provideMainScreenPresenter() {
        return new MapPresenter(service,
                ((RouterProvider) getParentFragment()).getRouter()
        );
    }*/


    public static MapScreenFragment getNewInstance(String name, int number) {
        MapScreenFragment fragment = new MapScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putInt(EXTRA_NUMBER, number);
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
        Wall mWall = room.getWalls().get(0);
        Float height = WinstrikeApp.getInstance().getDisplayHeightPx();
        Float width = WinstrikeApp.getInstance().getDisplayWidhtPx();

        Timber.d("Screen height in px: %s", height);
        Timber.d("Screen width in px: %s", width);

        mXScaleFactor = (width / mWall.getEnd().x) + 0.2f;
        mYScaleFactor = (height / mWall.getEnd().y) - 1.5f;

        Point seatSize = new Point();

        Bitmap seatBitmap = getBitmap(getContext(), R.drawable.ic_seat_gray);

        seatSize.set(seatBitmap.getWidth(), seatBitmap.getHeight());
        Point mScreenSize = MapViewUtils.Companion.calculateScreenSize(seatSize, room.getSeats(), mXScaleFactor, mYScaleFactor);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootLayout.getLayoutParams();
        params.setMargins(-50, -50, 100, 50);

        // Width and Height of screen
        if (height <= 1280) {
            params.width = mScreenSize.x;
            params.height = mScreenSize.y + 400;
            mYScaleFactor = (height / mWall.getEnd().y) - 1.5f;
            Timber.d("height: <= 1280");
        } else if (height <= 1920) {
            params.width = mScreenSize.x;
            params.height = mScreenSize.y + 300;
            mYScaleFactor = (height / mWall.getEnd().y) - 2.3f;
            Timber.d("height: <= 1920");
        } else if (height <= 2560) {
            params.width = mScreenSize.x;
            params.height = mScreenSize.y + 350;
            mYScaleFactor = (height / mWall.getEnd().y) - 3f;
            Timber.d("height: <= 2500");
        } else {
            params.width = mScreenSize.x;
            params.height = mScreenSize.y + 550;
            mYScaleFactor = (height / mWall.getEnd().y) - 1.5f;
            Timber.d("height: default");
        }
        rootLayout.setLayoutParams(params);

        rootLayout.setLayoutParams(params);


        for (Seat seat : room.getSeats()) {
            String name = seat.getName();
            String seatId = Utils.parseNumber(name);
            Integer seatIdInt = Integer.parseInt(seat.getId().toString()) + 1;
//            seatId = seatIdInt.toString();
            Integer idLenth = seatId.length();
            Integer textOffsetX = 0;
            Integer textOffsetY = -10;
            if (height <= 1280) {
                textOffsetY = 0;
            }

            Integer dx = 0;
            Integer dy = 0;


            Timber.d("Seat id: %s, idLenth: %s", seatIdInt, idLenth);


            // Calculate offset by x for diffrent length of numbers (1, 2 and 3)
            if (idLenth <= 1) {
                textOffsetX = 7;
            } else if (idLenth == 2) {
                textOffsetX = 20;
            } else if (idLenth == 3) {
                textOffsetX = 25;
            }

            if (width <= 720) {
                if (idLenth <= 1) {
                    textOffsetX = 7;
                } else if (idLenth == 2) {
                    textOffsetX = 10;
                } else if (idLenth == 3) {
                    textOffsetX = 25;
                }
            }

            dx = (int) ((seat.getDx() - MapViewUtils.Companion.getSeatOffsetX(seat)) * mXScaleFactor);
            dy = (int) ((seat.getDy() + MapViewUtils.Companion.getSeatOffsetY(seat)) * mYScaleFactor);

            // Seats numbers:
            tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
            tvParams.leftMargin = dx + seatSize.x / 2 - textOffsetX;
            tvParams.topMargin = dy + seatSize.y - textOffsetY - 10;

            Float angle = radianToDegrees(seat);
            if (height <= 1280) {
                if (angle != -90 && angle != 90) {
                    tvParams.topMargin = dy + seatSize.y - textOffsetY;
                }
            } else if (height <= 1920) {
                Timber.d("numbers: height <= 1920");
                if (angle != -90 && angle != 90) {
                    tvParams.topMargin = dy + seatSize.y - textOffsetY - 10;
                }

            } else {
                tvParams.topMargin = dy + seatSize.y - textOffsetY;
            }

            TextView textView = new TextView(getContext());

            textView.setText(seatId);
            if (height <= 1184) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextAppearance(R.style.StemRegular10Gray);
                } else {
                    textView.setTextAppearance(getContext(), R.style.StemRegular10Gray);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextAppearance(R.style.StemRegular12Gray);
                } else {
                    textView.setTextAppearance(getContext(), R.style.StemRegular12Gray);
                }
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
                LabelRoom label : room.getLabels())

        {
            String text = label.getText();
            Integer dx = 0;
            Integer dy = 0;

            dx = (int) ((label.getDx() - MapViewUtils.Companion.getLabelOffsetX(text)) * mXScaleFactor);
            dy = (int) ((label.getDy() + MapViewUtils.Companion.getLabelOffsetY(text)) * (mYScaleFactor));

            tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
            tvParams.leftMargin = dx;
            tvParams.topMargin = dy;
            if (height <= 1280) {
                tvParams.topMargin = dy - 5;
            }
            TextView textView = new TextView(getContext());
            textView.setText(text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(R.style.StemMedium17Primary);
            } else {
                textView.setTextAppearance(getContext(), R.style.StemMedium17Primary);
            }

            textView.setLayoutParams(tvParams);
            // Add horizontal line
            if (text.equals("HP STAGE 1")) {
                dx = (int) ((label.getDx() - MapViewUtils.Companion.getLabelOffsetX(text)) * mXScaleFactor);
                dy = (int) ((label.getDy() - MapViewUtils.Companion.getLabelOffsetY(text)) * (mYScaleFactor));
                tvDivParam = new RelativeLayout.LayoutParams(RLW, RLW);
                tvDivParam.leftMargin = dx;
                tvDivParam.topMargin = dy;
/*                if (height <= 1184) {
                    tvDivParam.leftMargin = dx;
                    tvDivParam.topMargin = dy - 320;
                } else if (height <= 1280) {
                    tvDivParam.leftMargin = dx;
                    tvDivParam.topMargin = dy - 420;
                } else if (height <= 1920) {
                    tvDivParam.leftMargin = dx;
                    tvDivParam.topMargin = dy - 650;
                } else if (height >= 2560) {
                    tvDivParam.leftMargin = dx;
                    tvDivParam.topMargin = dy - 1000;
                }*/
                if (height > 1184) {
/*                    View view = new View(getContext());
                    view.setBackgroundResource(R.drawable.hall_line);
                    view.setLayoutParams(tvDivParam);
                    rootLayout.addView(view);*/
                }
            }

            rootLayout.addView(textView);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Save selected seat's pids in db. For offline mode compatibility.
     *
     * @param id        - seat id
     * @param unselect  - is seat selected
     * @param publicPid - pid of selected seat
     */

    private void onSelectSeat(String id, boolean unselect, String publicPid) {

        if (!unselect) {
            mPickedSeatsIds.put(Integer.parseInt(id), publicPid);
        } else {
            mPickedSeatsIds.remove(Integer.parseInt(id));
        }

        TimeDataModel.INSTANCE.setPids(mPickedSeatsIds);

        Timber.d("startAt: %s", TimeDataModel.INSTANCE.getStart());
        Timber.d("endAt: %s", TimeDataModel.INSTANCE.getEnd());

        Timber.d("dateStart: %s", TimeDataModel.INSTANCE.getStartDate());
        Timber.d("dateEnd: %s", TimeDataModel.INSTANCE.getEndDate());

        Timber.d("isDateValid: %s", TimeDataModel.INSTANCE.isDateValid());

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
        seatParams.leftMargin = (int) ((seat.getDx() - MapViewUtils.Companion.getSeatOffsetX(seat)) * mXScaleFactor);
        seatParams.topMargin = (int) ((seat.getDy() + MapViewUtils.Companion.getSeatOffsetY(seat)) * mYScaleFactor);

        Float angle = radianToDegrees(seat);

        Timber.d("seat: %s, angle: %s", seat.getId(), angle);

        Float pivotX = seatBitmap.getWidth() / 2f;
        Float pivotY = seatBitmap.getHeight() / 2f;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.presenter = new MapPresenter(service, this);
        presenter.initScreen();
        presenter.readMap();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

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
        //TimeDataModel.INSTANCE.clearPids();

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
     * Something goes wrong, and we can't bye seat from Winstrike PC club.
     * show user toast with description this fucking situation.
     *
     * @param appErrorMessage
     */
    @Override
    public void onGetPaymentFailure(String appErrorMessage) {
        String timeFrom = TimeDataModel.INSTANCE.getStart();
        String timeTo = TimeDataModel.INSTANCE.getEnd();
        Timber.d("timeFrom: %s", timeFrom);
        Timber.d("timeTo: %s", timeTo);

        Timber.tag("common").w("Failure on pay: %s", appErrorMessage);
        if (appErrorMessage.contains("500")) toast("Внутренняя ошибка сервера: %s");
        if (appErrorMessage.contains("400")) toast("Нет данных: %s");
        if (appErrorMessage.contains("401")) toast("Пользователь не авторизован");
        if (appErrorMessage.contains("403")) toast("Ошибка авторизации пользователя");
        if (appErrorMessage.contains("404")) toast("В базе нет мест с таким public_id: %s");
        if (appErrorMessage.contains("405"))
            toast("Ошибка авторизации пользователя. Выйдите из приложение и зайдите снова.");
        if (appErrorMessage.contains("424")) toast("Не правильно выбрана дата");
        if (appErrorMessage.contains("416")) {
            toast("Не удается забронировать место на указанный интервал времени.");
        } else {
            toast("Не удается забронировать место.");
        }
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
