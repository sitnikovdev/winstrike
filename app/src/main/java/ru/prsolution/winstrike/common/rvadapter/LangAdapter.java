package ru.prsolution.winstrike.common.rvadapter;
/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.LangModel;
import ru.prsolution.winstrike.common.rvlistener.OnItemLangClickListener;

public class LangAdapter extends RecyclerView.Adapter<LangAdapter.LangViewHolder> {
    public static Integer SELECTED_ITEM = 0;
    private OnItemLangClickListener itemLangClickListener;
    private Context context;
    ArrayList<LangModel> langList;

    public LangAdapter(Context context, OnItemLangClickListener itemLangClickListener, ArrayList<LangModel> langList) {
        this.context = context;
        this.itemLangClickListener = itemLangClickListener;
        this.langList = langList;
    }


    @Override
    public LangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lang, parent, false);
        return new LangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LangViewHolder holder, int position) {
        LangModel lang = langList.get(position);
        holder.tv_lang.setText(lang.getName());
        if (position == SELECTED_ITEM) {
            holder.iv_checked.setImageResource(R.drawable.checked);
            holder.iv_checked.setVisibility(View.VISIBLE);
        }else {
            holder.iv_checked.setVisibility(View.GONE);
        }

        String langName = lang.getName();
        if (langName.equals("Русский")) {
            holder.iv_icon.setImageResource(R.drawable.ru_lang);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }else {
            holder.iv_icon.setImageResource(R.drawable.en_lang);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return langList.size();
    }


    class LangViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_language) TextView tv_lang;
        @BindView(R.id.image_lang_icon) ImageView iv_icon;
        @BindView(R.id.image_checked_ic) ImageView iv_checked;

        public LangViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemLangClickListener.onItemLangClick(view, getLayoutPosition());
        }
    }
}
