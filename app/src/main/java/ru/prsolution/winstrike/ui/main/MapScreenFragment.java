package ru.prsolution.winstrike.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.models.GameRoom;
import ru.prsolution.winstrike.mvp.models.LabelRoom;
import ru.prsolution.winstrike.mvp.models.Seat;
import ru.prsolution.winstrike.mvp.models.Wall;
import ru.prsolution.winstrike.mvp.presenters.MapPresenter;
import ru.prsolution.winstrike.mvp.views.MapView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.common.BackButtonListener;
import ru.prsolution.winstrike.ui.common.RouterProvider;
import timber.log.Timber;


/**
 * Created by terrakok 26.11.16
 */
public class MapScreenFragment extends MvpAppCompatFragment implements MapView, BackButtonListener {
    @BindView(R.id.rootMap)
    RelativeLayout rootLayout;
    private Snackbar snackbar;

    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_NUMBER = "extra_number";

    private final int RLW = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private RelativeLayout.LayoutParams tvParams;
    private RelativeLayout.LayoutParams seatParams;
    private RelativeLayout.LayoutParams rootLayoutParams;
    View drawView;

    int xFactor = 3;

    @Inject
    Service service;

    @InjectPresenter
    MapPresenter presenter;

    @ProvidePresenter
    MapPresenter provideMainScreenPresenter() {
        return new MapPresenter(service,
                ((RouterProvider) getParentFragment()).getRouter()
        );
    }

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
        Snackbar.SnackbarLayout snackLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackLayout.addView(snackView);
        snackLayout.setOnClickListener(new BookingBtnListener());
        snackbar.dismiss();
    }


    @Override
    public void onScreenInit() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        rootLayoutParams = new RelativeLayout.LayoutParams(RLW, RLW);

/*        if (dpHeight > 700) {
            xFactor = 4;
        } else {
            xFactor = 3;
        }*/
        initSnackBar();
    }

    @Override
    public void showLabel(List<LabelRoom> labels) {
        for (LabelRoom label : labels) {
            tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
            tvParams.leftMargin = label.getDx() * xFactor;
            tvParams.topMargin = (label.getDy() * xFactor) + 15;
            TextView tvLabel = new TextView(getContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvLabel.setTextAppearance(R.style.StemMedium17Primary);
            } else {
                tvLabel.setTextAppearance(getContext(), R.style.StemMedium17Primary);
            }
            tvLabel.setText(label.getText());
            tvLabel.setLayoutParams(tvParams);

            rootLayout.addView(tvLabel);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void showSeat(GameRoom room) {
        //drawSeat(room.getSeats());
        drawSeat(room);

/*        drawView = new DrawView(getContext(), room);
        ViewGroup.LayoutParams params = rootLayout.getLayoutParams();
        params.height = drawView.getMinimumHeight();
        params.width = drawView.getMinimumWidth();
        rootLayout.setLayoutParams(params);
        rootLayout.addView(drawView);*/
    }

    void drawSeat(GameRoom room) {
        Wall mWall = room.getWalls().get(0);
        Float height = WinstrikeApp.getInstance().getDisplayHeightPx();
        Float width = WinstrikeApp.getInstance().getDisplayWidhtPx();
        Float mXScaleFactor = (width / mWall.end.x);
        Float mYScaleFactor = (height / mWall.end.y);
        Point seatSize = new Point();

        Bitmap seatBitmap = getBitmap(getContext(), R.drawable.ic_seat_gray);

        seatSize.set(seatBitmap.getWidth(), seatBitmap.getHeight());
        Point mScreenSize = MapViewUtils.Companion.calculateScreenSize(seatSize, room.getSeats(), mXScaleFactor, mYScaleFactor);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootLayout.getLayoutParams();
        params.setMargins(0, 0, 100, 200);
        params.width = mScreenSize.x;
        params.height = mScreenSize.y + (seatSize.y * 15);
        rootLayout.setLayoutParams(params);

        rootLayout.setLayoutParams(params);


        for (Seat seat : room.getSeats()) {
            Double angle = Math.toDegrees(seat.getAngle());
            Float pivotX = seatBitmap.getWidth() / 2f;
            Float pivotY = seatBitmap.getHeight() / 2f;

            ImageView ivSeat = new ImageView(getContext());
            setImage(ivSeat, seat);

            seatParams = new RelativeLayout.LayoutParams(RLW, RLW);
            seatParams.leftMargin = (int) (seat.getDx() * mXScaleFactor);
            seatParams.topMargin = (int) (seat.getDy() * mYScaleFactor);

            ivSeat.setPivotX(pivotX);
            ivSeat.setPivotY(pivotY);
            ivSeat.setRotation(new Float(angle));

            ivSeat.setLayoutParams(seatParams);
            Timber.d("seat.type: %s", seat.getType());
            rootLayout.addView(ivSeat);
        }

        // Add labels
        for (LabelRoom label : room.getLabels()) {
            String text = label.getText();
            Integer dx = (int) (label.getDx() * mXScaleFactor);
            Integer dy = (int) (label.getDy() * (mYScaleFactor)) + seatSize.y/2;
            tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
            tvParams.leftMargin = dx;
            tvParams.topMargin =  dy;
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            textView.setLayoutParams(tvParams);
            // Add horizontal line
            if (text.equals("HP STAGE 1")) {
                tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
                tvParams.leftMargin = dx;
                tvParams.topMargin = (int) (dy - (seatSize.y * 20));
                View view = new View(getContext());
                view.setBackgroundResource(R.drawable.hall_line);
                view.setLayoutParams(tvParams);
                rootLayout.addView(view);

            }

            rootLayout.addView(textView);
        }

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
        presenter.initScreen();
        presenter.readMap();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
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
/*
        Intent browserIntent = new Intent(getContext(), YandexWebView.class);
        browserIntent.putExtra("url", url);
        startActivity(browserIntent);
*/
    }

    @Override
    public void onGetPaymentFailure(String appErrorMessage) {
        Timber.tag("common").w("Failure on pay: %s", appErrorMessage);
        if (appErrorMessage.contains("500")) toast("Внутренняя ошибка сервера: %s");
        if (appErrorMessage.contains("400")) toast("Нет данных: %s");
        if (appErrorMessage.contains("401")) toast("Пользователь не авторизован");
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


}
