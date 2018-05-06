package ru.prsolution.winstrike.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ru.prsolution.winstrike.mvp.apimodels.Label;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.Seat;
import ru.prsolution.winstrike.mvp.models.SeatType;
import ru.prsolution.winstrike.mvp.presenters.MapPresenter;
import ru.prsolution.winstrike.mvp.views.MapView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.common.BackButtonListener;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.prsolution.winstrike.ui.common.RouterProvider;
import timber.log.Timber;


/**
 * Created by terrakok 26.11.16
 */
public class MapScreenFragment extends MvpAppCompatFragment implements MapView, BackButtonListener {
    @BindView(R.id.rootMap)
    RelativeLayout mapLayout;
    private Snackbar snackbar;

    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_NUMBER = "extra_number";

    private final int RLW = RelativeLayout.LayoutParams.WRAP_CONTENT;
    private RelativeLayout.LayoutParams tvParams;
    private RelativeLayout.LayoutParams seatParams;

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
        snackbar = Snackbar.make(mapLayout, "", Snackbar.LENGTH_INDEFINITE);
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

        if (dpHeight > 700) {
            xFactor = 4;
        } else {
            xFactor = 3;
        }
        initSnackBar();
    }

    @Override
    public void showLabel(List<Label> labels) {
        for (Label label : labels) {
            tvParams = new RelativeLayout.LayoutParams(RLW, RLW);
            tvParams.leftMargin = label.getX() * xFactor;
            tvParams.topMargin = (label.getY() * xFactor) + 15;
            TextView tvLabel = new TextView(getContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvLabel.setTextAppearance(R.style.StemMedium17Primary);
            } else {
                tvLabel.setTextAppearance(getContext(), R.style.StemMedium17Primary);
            }
            tvLabel.setText(label.getText());
            tvLabel.setLayoutParams(tvParams);

            mapLayout.addView(tvLabel);
        }
    }

    public static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }

    /**
     * Set image selected on click. When selected - draw it white. Else, restore old image resource.
     *
     * @param ivSeat
     * @param seat
     * @param isSelected
     */

    @Override
    public void setSeatSelected(ImageView ivSeat, Seat seat, boolean isSelected) {
        String seatStatus = seat.getSeatStatus();
        SeatType status = SeatType.Companion.get(seatStatus);

        if (isSelected) {
            ivSeat.setBackgroundResource(R.drawable.seat_white);
            presenter.showSnackBar();
        } else {

            if (status == SeatType.FREE) {
                ivSeat.setImageResource(R.drawable.seat_grey);
            }
            if (status == SeatType.BOOKING) {
                ivSeat.setImageResource(R.drawable.seat_red);
            }
            if (status == SeatType.SELF_BOOKING) {
                ivSeat.setImageResource(R.drawable.seat_blue);
            }
            if (status == SeatType.VIP) {
                ivSeat.setImageResource(R.drawable.seat_yellow);
            }
            if (status == SeatType.HIDDEN) {
                ivSeat.setImageResource(R.drawable.seat_darkgrey);
            }

            rotateSeat(seat, ivSeat);

            presenter.hideSnackBar();
        }
    }

    @Override
    public void showSeat(List<Seat> seats) {
        for (Seat seat : seats) {
            ImageView ivSeat = new ImageView(getContext());

            SeatType seatStatus = SeatType.Companion.get(seat.getSeatStatus());
            ivSeat.setBackgroundResource(seatStatus.getImage());

            seatParams = new RelativeLayout.LayoutParams(RLW, RLW);
            seatParams.leftMargin = seat.getSeatXLeft() * xFactor;
            seatParams.topMargin = seat.getSeatYTop() * xFactor;

            rotateSeat(seat, ivSeat);
            ivSeat.setLayoutParams(seatParams);

            ivSeat.setOnClickListener(
                    v -> onSeatClicked(seat, ivSeat)
            );
            mapLayout.addView(ivSeat);
        }
    }

    private void onSeatClicked(Seat seat, ImageView ivSeat) {
/*        String publicId = seat.getPublic_id();
        List<String> savedPublicIds = MapInfoSingleton.getInstance().getPidArray();
        for (String pid : savedPublicIds){
            if (publicId.equals(pid)) {
                seat.setSelected(false);
                MapInfoSingleton.getInstance().getPidArray().remove(publicId);
            }
        }
        */
        setSeatSelected(ivSeat, seat, seat.isSelected());
//        if (seat.getSeatType() == 0 || seat.getSeatType() == 1) {
            if (!seat.isSelected()) {
                seat.setSelected(true);
                MapInfoSingleton.getInstance().addToArray(seat.getPublic_id());
            } else {
                seat.setSelected(false);
                MapInfoSingleton.getInstance().getPidArray().remove(seat.getPublic_id());
            }
            setSeatSelected(ivSeat, seat, seat.isSelected());
//        }

    }

    private void rotateSeat(Seat seat, ImageView ivSeat) {
        if (Math.signum(seat.getSeatAngle()) == 1.0) {
            ivSeat.setRotation(180);
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
}
