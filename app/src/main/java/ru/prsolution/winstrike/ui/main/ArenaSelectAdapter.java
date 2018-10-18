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
  private OnItemArenaClickListener itemArenaClickListener;

  interface OnItemArenaClickListener {

    void onArenaSelectItem(View v, int layoutPosition);
  }

  private Context mContext;
  ArrayList<RowItem> rowItems;

  public ArenaSelectAdapter(Context context, OnItemArenaClickListener itemArenaClickListener, ArrayList<RowItem> rowItems) {
    this.mContext = context;
    this.itemArenaClickListener = itemArenaClickListener;
    this.rowItems = rowItems;
  }


  @Override
  public LangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_arena, parent, false);
    return new LangViewHolder(view);
  }

  @Override
  public void onBindViewHolder(LangViewHolder holder, int position) {
    RowItem item = rowItems.get(position);
    holder.tv_title.setText(item.getTitle());
    holder.tv_addres.setText(item.getAddress());
    if (position == SELECTED_ITEM) {
      holder.tv_title.setTextColor(mContext.getColor(R.color.color_accent));
      holder.tv_addres.setTextColor(mContext.getColor(R.color.color_accent));
      holder.iv_checked.setImageResource(R.drawable.ic_checked);
      holder.iv_checked.setVisibility(View.VISIBLE);
    } else {
      holder.tv_title.setTextColor(mContext.getColor(R.color.color_black));
      holder.tv_addres.setTextColor(mContext.getColor(R.color.color_black));
      holder.iv_checked.setVisibility(View.GONE);
    }

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
      itemArenaClickListener.onArenaSelectItem(view, getLayoutPosition());
    }
  }
}
