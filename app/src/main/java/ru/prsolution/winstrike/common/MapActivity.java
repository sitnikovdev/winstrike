package ru.prsolution.winstrike.common;
/*
 * Created by oleg on 04.03.2018.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.utils.ClearableEditText;
import ru.prsolution.winstrike.common.utils.TextFormat;
import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.Room;
import ru.prsolution.winstrike.oldapi.ApiService;
import ru.prsolution.winstrike.oldapi.ApiServiceImpl;
import ru.prsolution.winstrike.ui.common.HtmlViewer;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import timber.log.Timber;

public class MapActivity extends AppCompatActivity {
    private ApiService apiService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    Room room;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_map);
        ButterKnife.bind(this);

        apiService = ApiServiceImpl.getNewInstance(this).getApi();
/*        tinyDB = new TinyDB(this);
        rootLayout = findViewById(R.id.root);
        tinyDB.putBoolean("seatSelected", false);

        initScreen();
        initSnackBar();*/

        String mapJsonStr;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mapJsonStr = null;
            } else {
                mapJsonStr = extras.getString("map");
            }
        } else {
            mapJsonStr = (String) savedInstanceState.getSerializable("map");
        }
        //Parse json file:
//        readMap(rootLayout);

        Timber.tag("Map-->").d("MapActivity - onCreate getActivePid:  %s", mapJsonStr);

//        msgDlg();
        initToolBar();

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(
                it -> startActivity(new Intent(this, MainScreenActivity.class))
        );
        toolbar_title.setText(R.string.arena_name);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_help:
                legendDlg();
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.map_toolbar_menu);
        return true;
    }

    public String dateFormatToUTC(Date date) {
        String selectDate;
        selectDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ").format(date);
        return selectDate;
    }

    private void bookingDlg() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_booking);
        View vTapClose = dialog.findViewById(R.id.v_tapclose);
        View vPayBtn = dialog.findViewById(R.id.v_paybtn);
        ClearableEditText etCode = dialog.findViewById(R.id.et_code);

        TextFormat.formatText(etCode, "(___) ___-__-__");

        vTapClose.setOnClickListener(
                it -> dialog.dismiss()
        );

        PaymentModel payModel;
        payModel = new PaymentModel();
        Timber.tag("OkHttp").d("Date: %s", dateFormatToUTC(new Date()));

        payModel.setStartAt(MapInfoSingleton.getInstance().getDateFromShort());
        payModel.setEnd_at(MapInfoSingleton.getInstance().getDateToShort());

        payModel.setPlacesPid(MapInfoSingleton.getInstance().getPidArray());

        Timber.tag("OkHttp").d("Payment: %s", payModel);
        room = new Room();
        vPayBtn.setOnClickListener(
                it -> {
                    Timber.tag("OkHttp").e("On Pay button  click Pids: %s", MapInfoSingleton.getInstance().getPidArray());
//                    pay(payModel);
                    dialog.dismiss();
                }
        );

        Timber.tag("OkHttp").d("room active id: %s", room.getActiveLayoutPid());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 200;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }


    private void legendDlg() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_legend);
        TextView tvSee = dialog.findViewById(R.id.tv_see);

        tvSee.setOnClickListener(
                it -> dialog.dismiss()
        );


        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 200;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }

    private void msgDlg() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_msg);
        TextView tvSee = dialog.findViewById(R.id.tv_see);

//        tinyDB.putString("active_layout_pid", "");
        tvSee.setOnClickListener(
                it -> {
                    dialog.dismiss();
//                    getActivePid();
//                    Timber.tag("OkHttp").d("active_layout_pid: %s", tinyDB.getString("active_layout_pid"));
//                    dialog.dismiss();
//                    Timber.tag("OkHttp").d("Map: Active arena pid: %s", ServiceGenerator.arenaPid);
                }
        );


        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 200;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }
/*

    public void pay(PaymentModel paymentModel) {
        tinyDB.putString("operation", "pay");


        apiService.createPayment(paymentModel)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(userModelResponse -> {
                    if (userModelResponse.isSuccessful()) {
                        Timber.tag("OkHttp").d("Successful payment: %s", userModelResponse.code());
                        tinyDB.putObject("message", userModelResponse.message());
                    } else {
                        switch (userModelResponse.code()) {
                            case 500:
                                Timber.tag("OkHttp").e("Внутренняя ошибка сервера: %s", userModelResponse.errorBody().string());
                                toast("Внутренняя ошибка сервера: " + userModelResponse.raw().code());
                                break;
                            case 400:
                                Timber.tag("OkHttp").d("Нет данных: %s", userModelResponse.code());
                                toast("Нет данных");
                                break;
                            case 401:
                                Timber.tag("OkHttp").d("Нет данных в формате json: %s", userModelResponse.code());
                                toast("Нет данных в формате json");
                                break;
                            case 404:
                                Timber.tag("OkHttp").d("В базе нет мест с таким public_id: %s", userModelResponse.code());
                                toast("В базе нет мест с таким public_id");
                                break;
                            default:
                                Timber.tag("OkHttp").d("Ошибка с кодом: %s", userModelResponse.code());
                                toast("Ошибка входа: " + userModelResponse.code());
                                break;
                        }
                    }

                }, error -> {
                    if (error instanceof HttpException) {
                        Response response = ((HttpException) error).response();
                        Timber.tag("OkHttp").d(" Обработка ошибки http: %s", response.code());
                    } else {
                        error.printStackTrace();
                    }
                });
    }
*/

    private void showErrorHtml(Response<List> userModelResponse) {
        Intent intent = new Intent(getBaseContext(), HtmlViewer.class);
        intent.putExtra("response", userModelResponse.raw().message() + ": " + userModelResponse.raw().code());
        startActivity(intent);
    }

    private void showSuccessHtml(Response<List> userModelResponse) {
        Intent intent = new Intent(getBaseContext(), HtmlViewer.class);
        intent.putExtra("response", userModelResponse.message() + ": " + userModelResponse.code());
        startActivity(intent);
    }

    private void toast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

}
