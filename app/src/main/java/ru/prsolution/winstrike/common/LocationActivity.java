package ru.prsolution.winstrike.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.rvadapter.CityAdapter;
import ru.prsolution.winstrike.common.rvadapter.LangAdapter;
import ru.prsolution.winstrike.common.rvlistener.OnItemCityClickListener;
import ru.prsolution.winstrike.common.rvlistener.OnItemLangClickListener;
import ru.prsolution.winstrike.common.entity.CityModel;
import ru.prsolution.winstrike.common.entity.LangModel;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import rx.Observable;

import static android.view.View.VISIBLE;

/*
 * Created by oleg on 31.01.2018.
 */

public class LocationActivity extends AppCompatActivity implements OnItemCityClickListener, OnItemLangClickListener {
    @BindView(R.id.root)
    ConstraintLayout root;
    private ArrayList<CityModel> cities = new ArrayList<>();
    private ArrayList<LangModel> languages = new ArrayList<>();



    private ConstraintSet rootConstraintSet = new ConstraintSet();
    private ConstraintSet cityUpConstraintSet = new ConstraintSet();
    private ConstraintSet cityDownConstraintSet = new ConstraintSet();

    private ConstraintSet langUpConstraintSet = new ConstraintSet();
    private ConstraintSet langDownConstraintSet = new ConstraintSet();
    private SharedPreferences preferences;
    private static final String APP_PREFERENCES_FILE = "settings";

    //City
    @BindView(R.id.fl_arrow__tap_up_city)
    View viewCityUp;
    @BindView(R.id.fl_arrow_tap_down_city)
    View viewCityDown;
    @BindView(R.id.text_city_title)
    TextView tv_city;
    @BindView(R.id.image_loc_city_icon)
    ImageView iv_loc;
    @BindView(R.id.rv_cities_list)
    RecyclerView rv_city;

    //Lang
    @BindView(R.id.fl_arrow_tap_up_lang)
    View viewLangUp;
    @BindView(R.id.fl_arrow_tap_down_lang)
    View viewLangDown;
    @BindView(R.id.text_lang_title)
    TextView tv_lang;
    @BindView(R.id.rv_languages_list)
    RecyclerView rv_lang;
    @BindView(R.id.image_lang_ru_icon) ImageView iv_ru;
    @BindView(R.id.image_lang_en_icon) ImageView iv_en;

    //Next button
    @BindView(R.id.next_button) View next_button;
    @BindView(R.id.text_button_title) TextView tv_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_location);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(APP_PREFERENCES_FILE, Context.MODE_PRIVATE);


        // force-disable the next button
        next_button.setVisibility(View.GONE);
        tv_button.setVisibility(View.GONE);
        next_button.setOnClickListener(
                it-> startActivity(new Intent(this, SignInActivity.class))
        );

        //Transitions animations:
        rootConstraintSet.clone(this, R.layout.ac_location);
        cityDownConstraintSet.clone(this, R.layout.part_location_city_dn);
        cityUpConstraintSet.clone(this, R.layout.part_location_city_up);

        langDownConstraintSet.clone(this, R.layout.part_location_lang_dn);
        langUpConstraintSet.clone(this, R.layout.part_location_lang_up);

        //City recycler view init:
        fillCitiesList();
        rv_city.setAdapter(new CityAdapter(this, this, cities));
        rv_city.setLayoutManager(new LinearLayoutManager(this));
        rv_city.getAdapter().notifyDataSetChanged();

        //Lang recycler view init:
        fillLangList();
        rv_lang.setAdapter(new LangAdapter(this, this, languages));
        rv_lang.setLayoutManager(new LinearLayoutManager(this));
        rv_lang.getAdapter().notifyDataSetChanged();

        //Cities select:
        viewCityDown.setOnClickListener(
                v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(root);
                    }
                    cityDownConstraintSet.applyTo(root);
                    langUpConstraintSet.applyTo(root);
                    isCitySelected();
                }
        );
        viewCityUp.setOnClickListener(
                v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(root);
                    }
                    cityUpConstraintSet.applyTo(root);
                    isCitySelected();
                }
        );

        // Lang select:
        viewLangDown.setOnClickListener(
                v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(root);
                    }
                    langDownConstraintSet.applyTo(root);
                    cityUpConstraintSet.applyTo(root);
                    isLangSelected();
                }
        );
        viewLangUp.setOnClickListener(
                v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(root);
                    }
                    langUpConstraintSet.applyTo(root);
                    isLangSelected();
                }
        );

        Observable<TextViewTextChangeEvent> cityObservable = RxTextView.textChangeEvents(tv_city);
        Observable<TextViewTextChangeEvent> langObservable = RxTextView.textChangeEvents(tv_lang);


        Observable.combineLatest(cityObservable, langObservable, (citySelected, langSelected) -> {
            boolean cityCheck = citySelected.text().equals("Выберите город");
            boolean langCheck = langSelected.text().equals("Выберите язык");
            return !cityCheck && !langCheck;
        }).subscribe(aBoolean -> {
            if (aBoolean) {
                tv_button.setVisibility(VISIBLE);
                next_button.setVisibility(VISIBLE);
            }
        });
    }

    @Override
    public void onItemCityClick(View view, Integer position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(root);
        }
        cityUpConstraintSet.applyTo(root);

        CityAdapter.SELECTED_ITEM = position;
        CityModel city = cities.get(position);
        tv_city.setText(city.getName());
        iv_loc.setVisibility(VISIBLE);
        rv_city.getAdapter().notifyDataSetChanged();

        preferences.edit()
                .putString("city",city.getName())
                .apply();
    }

    @Override
    public void onItemLangClick(View view, Integer position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(root);
        }
        langUpConstraintSet.applyTo(root);

        LangAdapter.SELECTED_ITEM = position;
        LangModel lang = languages.get(position);
        tv_lang.setText(lang.getName());

        isLangSelected();
        rv_lang.getAdapter().notifyDataSetChanged();

        preferences.edit()
                .putString("lang",lang.getName())
                .apply();

    }

    private void isLangSelected() {
        Log.d("lang", "tv_lang text: " + tv_lang.getText());
        String lang = tv_lang.getText().toString().trim();
        if (lang.contains("Русский")) {
            Log.d("lang", "Выбран русский");
            iv_ru.setVisibility(VISIBLE);
            iv_en.setVisibility(View.GONE);
        } else if (lang.contains("English")) {
            Log.d("lang", "Выбран английский");
            iv_en.setVisibility(VISIBLE);
            iv_ru.setVisibility(View.GONE);
        }
    }

    private void isCitySelected() {
        if (tv_city.getText().equals("Выберите город")) {
            iv_loc.setVisibility(VISIBLE);
        }
    }

    private void fillCitiesList() {
        cities.add(new CityModel("Москва"));
        cities.add(new CityModel("Санкт-Петербург"));
        cities.add(new CityModel("Н.Новгород"));
    }

    private void fillLangList() {
        languages.add(new LangModel("Русский"));
        languages.add(new LangModel("English"));
    }

}
