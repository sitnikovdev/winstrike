package ru.prsolution.winstrike.common.rvadapter;
/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.CityModel;
import ru.prsolution.winstrike.common.rvlistener.OnItemCityClickListener;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    public static Integer SELECTED_ITEM = 0;
    private OnItemCityClickListener itemCityClickListener;
    private Context context;
    ArrayList<CityModel> cityList;

    public CityAdapter(Context context, OnItemCityClickListener itemCityClickListener, ArrayList<CityModel> cityList) {
        this.context = context;
        this.itemCityClickListener = itemCityClickListener;
        this.cityList = cityList;
    }


    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cities, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        CityModel city = cityList.get(position);
        holder.cityName.setText(city.getName());
        if (position == SELECTED_ITEM) {
            holder.cityName.setTextColor(Color.parseColor("#ffc5166c"));
        } else {
            holder.cityName.setTextColor(Color.parseColor("#ff161715"));
        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }


    class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cityName;

        public CityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cityName = itemView.findViewById(R.id.text_city_title);
        }

        @Override
        public void onClick(View view) {
            itemCityClickListener.onItemCityClick(view, getLayoutPosition());
        }
    }
}
