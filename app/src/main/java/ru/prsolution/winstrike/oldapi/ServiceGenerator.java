package ru.prsolution.winstrike.oldapi;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.prsolution.winstrike.common.utils.HtmlErrorPages;
import ru.prsolution.winstrike.common.utils.TinyDB;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.ui.common.HtmlViewer;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.prsolution.winstrike.ui.common.YandexWebView;
import timber.log.Timber;

import static ru.prsolution.winstrike.common.utils.Utils.formatTime;
import static ru.prsolution.winstrike.common.utils.Utils.getFormattedDate;

/**
 * Created by designer on 06/03/2018.
 */


public class ServiceGenerator {


    public interface OnConnectionTimeoutListener {
        void onConnectionTimeout();
    }


    private static OnConnectionTimeoutListener listener;

    private static Context mContext;
    public static String arenaPid;
    private static TinyDB tinyDB;
    private static final String BASE_URL = "http://82.202.213.163/api/v1/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());


    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder().addInterceptor(chain -> {
                Request original = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("host", "winstrike.ru")
                        .build();

                Response response = chain.proceed(original);
                switch (response.message()) {
                    case "UNAUTHORIZED":
                        response = chain.proceed(original.newBuilder()
                                .addHeader("Authorization", "Bear " + tinyDB.getString("token")).build());
                        break;
                    case "NOT ACCEPTABLE":
                        Timber.tag("OkHttp").d(response.message());
                        break;
                    case "CREATED":
                        Timber.tag("OkHttp").d(response.message());
                        break;
                    default:
                        Timber.tag("OkHttp").d(response.message());
                        break;
                }

                String operation = tinyDB.getString("operation");
                if (operation.equals("sendsms")) {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    if (response.isSuccessful()) {
                        Timber.d("Sms send successfully to user");
                    } else {
                        Timber.w("Error sending sms code to user: %s", response.message());
                    }
                }
                if (operation.equals("confirm")) {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    if (response.isSuccessful()) {
                        Timber.d("UserEntity successfully confirmed");
                    } else {
                        Timber.w("UserEntity confirmed error: %s", response.message());
                    }

                }
                if (operation.equals("getplaces")) {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    try {
                        if (response.isSuccessful()) {
                             List<OrderModel> mPayList = new ArrayList<>();
                            JSONObject jobject = new JSONObject(responseBodyCopy.string());
                            JSONObject user = jobject.getJSONObject("user");
                            JSONArray orders = user.getJSONArray("orders");
                            // Get all orders for this computer

                            // Ouputs Computers Info:
                            for (int o = 0; o < orders.length(); o++) {
                                JSONObject order = orders.getJSONObject(o);
                                String user_pid = order.getString("user_pid");
                                if (user_pid.equals(tinyDB.getString("public_id"))) {
                                    JSONObject compPlace = order.getJSONObject("place");
                                    JSONObject comp = compPlace.getJSONObject("computer");
                                    String pcName = comp.getString("name");

                                    String cost = order.getString("cost");
                                    String start_at = order.getString("start_at");
                                    String end_at = order.getString("end_at");

                                    String date = getFormattedDate(start_at);
                                    String time = formatTime(start_at) + "-" + formatTime(end_at);

                                    String access_code = order.getString("access_code");
//                                    OrderModel orderModel = new OrderModel(mContext.getResources().getString(R.string.club_name), cost, date, time, pcName, access_code, R.drawable.main_screen);
//                                    mPayList.add(orderModel);
                                }
                            }
                            MapInfoSingleton.getInstance().setPayList(mPayList);

                        } else {
                            Timber.tag("common").e("Error user createUser: %s  %s", response.code(), response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (operation.equals("createUser")) {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    try {
                        if (response.isSuccessful()) {
                            JSONObject jobject = new JSONObject(responseBodyCopy.string());
                            String url = jobject.getString("redirect_url");
                        } else {
                            Timber.tag("common").e("Error user createUser: %s  %s", response.code(), response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (operation.equals("pay")) {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    Intent browserIntent = new Intent(mContext, YandexWebView.class);
                    try {
                        if (response.isSuccessful()) {
                            JSONObject jobject = new JSONObject(responseBodyCopy.string());
                            String url = jobject.getString("redirect_url");
                            browserIntent.putExtra("url", url);
//                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        } else {
//                            browserIntent.putExtra("url", url);
                            browserIntent = new Intent(mContext, HtmlViewer.class);
                            browserIntent.putExtra("response", HtmlErrorPages.getPage(String.valueOf(response.code())));
                        }
                        mContext.startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Get MAP
                // Get places for map:
                // 1) get active layout pid
                // 2) get room_layouts (map)
                if (operation.equals("arena_pid") || operation.equals("payments")) {
                    // dirty hack for OkHttp
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    JSONObject Jobject = null;
                    String timeFrom = null;
                    String timeTo;

                    try {
                        Jobject = new JSONObject(responseBodyCopy.string());
                        JSONObject room = Jobject.getJSONObject("room");
                        String active_layout_pid = room.getString("active_layout_pid");
                        Timber.tag("OkHttp").d("Service Generator: Active layout pid: %s", active_layout_pid);
                        /**
                         * For test:
                         */
/*						MapInfoSingleton.getInstance().setDateFromShort("2018-04-04T14:00:00");
                        MapInfoSingleton.getInstance().setDateToShort("2018-04-04T18:00:00");*/

                        timeFrom = tinyDB.getString("timeFrom");
                        timeTo = tinyDB.getString("timeTo");


                        if (timeFrom == null || timeFrom.isEmpty()) {
/*							timeFrom = MapInfoSingleton.getInstance().getDateFromShort();
							timeTo = MapInfoSingleton.getInstance().getDateToShort();*/
                            return response;
                        }
/*
						if (TextUtils.isEmpty(timeFrom)) {
							tinyDB.putString("timeFrom", timeFrom);
							tinyDB.putString("timeTo", timeTo);
						}
*/
                        tinyDB.putString("active_layout_pid", active_layout_pid);
                        arenaPid = active_layout_pid;
                        String url_req = BASE_URL + "room_layouts/" + active_layout_pid + "?start_at=" + timeFrom +
                                "&end_at=" + timeTo;
                        response = chain.proceed(original.newBuilder()
                                .url(url_req)
                                .addHeader("Authorization", "Bear " + tinyDB.getString("token")).build());
                        if (response.code() == 416) {
                            Timber.tag("common").d("Не удалось получить места на указанный период времени. Уточните  расписание работы клуба.");
                            return response;
                        }
                        if (response.code() == 500){
                            Timber.tag("common").d("Невозможно получить данные. Внутренняя ошибка сервера.");
                            return response;
                        }
                        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
                        Jobject = new JSONObject(responseBody.string());
                        JSONObject joMap = Jobject.getJSONObject("room_layout");
                        String mapArena = joMap.toString();
                        MapInfoSingleton.getInstance().setRooms(mapArena);

//                        tinyDB.putString("map", mapArena);

                    } catch (JSONException e) {
                        Timber.e("ServiceGenerator - error on get map: %s", e.getCause());
                        e.printStackTrace();
                    }
                }
                return response;
            });


    public static <S> S createService(
            Class<S> serviceClass, Context context) {
        mContext = context;
        tinyDB = new TinyDB(mContext);
        tinyDB.putString("operation", "");
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
//        httpClient.interceptors().add(chain -> onIntercept(chain));
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }

}

