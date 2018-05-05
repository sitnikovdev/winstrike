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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.ArenaModel;
import ru.prsolution.winstrike.common.rvlistener.OnItemArenaClickListener;

public class ArenaAdapter extends RecyclerView.Adapter<ArenaAdapter.ArenaViewHolder> {
    public static Integer SELECTED_ITEM = 0;
    private OnItemArenaClickListener itemArenaClickListener;
    private Context context;
    List<ArenaModel> arenaList;

    public ArenaAdapter(Context context, OnItemArenaClickListener itemArenaClickListener, List<ArenaModel> arenaList) {
        this.context = context;
        this.itemArenaClickListener = itemArenaClickListener;
        this.arenaList = arenaList;
    }


    @Override
    public ArenaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_arena, parent, false);
        return new ArenaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArenaViewHolder holder, int position) {
        ArenaModel arena = arenaList.get(position);
        holder.text_arena.setText(arena.getName());
        holder.image_lang_icon.setImageResource(R.drawable.ic_arena);
        holder.image_lang_icon.setVisibility(View.VISIBLE);
        if (position == SELECTED_ITEM) {
            holder.text_arena.setTextColor(Color.parseColor("#ffc5166c"));
/*            holder.checkedIcon.setImageResource(R.drawable.checked);
            holder.checkedIcon.setVisibility(View.VISIBLE);*/
        } else {
            holder.text_arena.setTextColor(Color.parseColor("#ff161715"));
//            holder.checkedIcon.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return arenaList.size();
    }


    class ArenaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_arena)
        TextView text_arena;
        @BindView(R.id.image_lang_icon)
        ImageView image_lang_icon;
        @BindView(R.id.image_checked_ic)
        ImageView checkedIcon;

        public ArenaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemArenaClickListener.onItemArenaClick(view, getLayoutPosition());
        }
    }
}
