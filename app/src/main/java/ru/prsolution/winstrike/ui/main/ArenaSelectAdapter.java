package ru.prsolution.winstrike.ui.main;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import ru.prsolution.winstrike.R;

public class ArenaSelectAdapter extends RecyclerView.Adapter<ArenaSelectAdapter.LangViewHolder> {
    public static Integer SELECTED_ITEM = 0;
    private OnItemLangClickListener itemLangClickListener;
    interface OnItemLangClickListener {
        void onSelectItem(View v, int layoutPosition);
    }
    private Context context;
    ArrayList<RowItem> rowItems;

    public ArenaSelectAdapter(Context context, OnItemLangClickListener itemLangClickListener, ArrayList<RowItem> rowItems) {
        this.context = context;
        this.itemLangClickListener = itemLangClickListener;
        this.rowItems = rowItems;
    }


    @Override
    public LangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_arena, parent, false);
        return new LangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LangViewHolder holder, int position) {
        RowItem item = rowItems.get(position);
        holder.tv_title.setText(item.getTitle());
        if (position == SELECTED_ITEM) {
            holder.iv_checked.setImageResource(R.drawable.ic_checked);
            holder.iv_checked.setVisibility(View.VISIBLE);
        }else {
            holder.iv_checked.setVisibility(View.GONE);
        }

        String langName = item.getTitle();
/*        if (langName.equals("Русский")) {
            holder.tv_addres.setText(item.getAddress());
            holder.tv_addres.setVisibility(View.VISIBLE);
        }else {
            holder.tv_addres.setImageResource(R.drawable.en_lang);
            holder.tv_addres.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return rowItems.size();
    }


    class LangViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title) TextView tv_title;
        @BindView(R.id.address) TextView tv_addres;
        @BindView(R.id.iv_checked) ImageView iv_checked;

        public LangViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemLangClickListener.onSelectItem(view, getLayoutPosition());
        }
    }
}
