package ru.prsolution.winstrike.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import ru.prsolution.winstrike.db.PidViewModel;
import ru.prsolution.winstrike.db.entity.PidEntity;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.common.MapViewUtils;
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
    private PidViewModel mPidViewModel;
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


        if (heightDp > 700) {
            mXScaleFactor = (width / mWall.end.x) - 0.3f;
            mYScaleFactor = (height / mWall.end.y) - 2.3f;
        } else {
            mXScaleFactor = (width / mWall.end.x) - 0.3f;
            mYScaleFactor = (height / mWall.end.y) - 2.3f;
        }
        Point seatSize = new Point();

        Bitmap seatBitmap = getBitmap(getContext(), R.drawable.ic_seat_gray);

        seatSize.set(seatBitmap.getWidth(), seatBitmap.getHeight());
        Point mScreenSize = MapViewUtils.Companion.calculateScreenSize(seatSize, room.getSeats(), mXScaleFactor, mYScaleFactor);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootLayout.getLayoutParams();
        params.setMargins(0, 0, 100, 200);

        // Height of screen
        if (heightDp > 700) {
            params.width = mScreenSize.x;
            params.height = mScreenSize.y + 700;
        } else {
            params.width = mScreenSize.x;
            params.height = mScreenSize.y + 500;
        }
        rootLayout.setLayoutParams(params);

        rootLayout.setLayoutParams(params);


        for (Seat seat : room.getSeats()) {
            ImageView ivSeat = new ImageView(getContext());
            setImage(ivSeat, seat);

            rotateSeat(seatBitmap, seat, ivSeat);

            ivSeat.setLayoutParams(seatParams);

            View.OnClickListener mSeatViewOnClickListener = new mSeatViewOnClickListener(seat, ivSeat, seatBitmap, mPickedSeatsIds);
            ivSeat.setOnClickListener(mSeatViewOnClickListener);
            rootLayout.addView(ivSeat);
        }


        // Add labels
        for (LabelRoom label : room.getLabels()) {
            String text = label.getText();
            Integer dx = 0;
            Integer dy = 0;

            if (heightDp > 700) {
                dx = (int) (label.getDx() * mXScaleFactor) - 1;
                dy = (int) (label.getDy() * (mYScaleFactor));
            } else {
                dx = (int) (label.getDx() * mXScaleFactor) - 1;
                dy = (int) (label.getDy() * (mYScaleFactor)) - 10;
            }

            tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
            tvParams.leftMargin = dx;
            tvParams.topMargin = dy;
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
                tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
                if (heightDp > 700) {
                    tvParams.leftMargin = dx;
                    tvParams.topMargin = dy - 800;
                } else {
                    tvParams.leftMargin = dx;
                    tvParams.topMargin = dy - 650;
                }
                View view = new View(getContext());
                view.setBackgroundResource(R.drawable.hall_line);
                view.setLayoutParams(tvParams);
                rootLayout.addView(view);
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
        PidEntity pidEntity = new PidEntity();
        if (!unselect) {
            mPickedSeatsIds.put(Integer.parseInt(id), publicPid);
            pidEntity.setId(Integer.parseInt(id));
            pidEntity.setPublickId(publicPid);
            //mPidViewModel.insert(pidEntity);
        } else {
            mPickedSeatsIds.remove(Integer.parseInt(id));
            //mPidViewModel.delete(Integer.parseInt(id));
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
        seatParams.leftMargin = (int) (seat.getDx() * mXScaleFactor);
        seatParams.topMargin = (int) (seat.getDy() * mYScaleFactor);

        Float angle = radianToDegrees(seat);
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
            presenter.onBookingClick();
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
        presenter.onPaySuccess(url);
    }

    /**
     * Something goes wrong, and we can't bye seat from Winstrike PC club.
     * show user toast with description this fucking situation.
     *
     * @param appErrorMessage
     */
    @Override
    public void onGetPaymentFailure(String appErrorMessage) {
        Timber.tag("common").w("Failure on pay: %s", appErrorMessage);
        if (appErrorMessage.contains("500")) toast("Внутренняя ошибка сервера: %s");
        if (appErrorMessage.contains("400")) toast("Нет данных: %s");
        if (appErrorMessage.contains("401")) toast("Пользователь не авторизован");
        if (appErrorMessage.contains("403")) toast("Ошибка авторизации пользователя");
        if (appErrorMessage.contains("404")) toast("В базе нет мест с таким public_id: %s");
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

    private void changeTheme(final Resources.Theme theme, ImageView imageView) {
        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_seat_exp, theme);
        imageView.setImageDrawable(drawable);
    }

    private class mSeatViewOnClickListener implements View.OnClickListener {

        private final ImageView ivSeat;
        private Seat seat;
        private Bitmap seatBitmap;
        private LinkedHashMap mPickedSeatsIds;

        public mSeatViewOnClickListener(Seat seat, ImageView ivSeat, Bitmap seatBitmap, LinkedHashMap mPickedSeatsIds) {
            this.seat = seat;
            this.ivSeat = ivSeat;
            this.seatBitmap = seatBitmap;
            this.mPickedSeatsIds = mPickedSeatsIds;
        }

        @Override
        public void onClick(View v) {
            if (seat.getType() == SeatType.FREE || seat.getType() == SeatType.VIP) {
                if (!mPickedSeatsIds.containsKey(Integer.parseInt(seat.getId()))) {
                    ivSeat.setBackgroundResource(R.drawable.ic_seat_picked);
                    onSelectSeat(seat.getId(), false, seat.getPublicPid());
                    Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.getId(), seat.getType(), seat.getPcname(), seat.getPublicPid());
                } else {
                    rootLayout.removeView(ivSeat);
                    setImage(ivSeat, seat);
                    rotateSeat(seatBitmap, seat, ivSeat);
                    rootLayout.addView(ivSeat);
                    onSelectSeat(seat.getId(), true, seat.getPublicPid());
                }
            } else {
                Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.getId(), seat.getType(), seat.getPcname(), seat.getPublicPid());
                animateView(ivSeat);
            }
        }
    }

}
