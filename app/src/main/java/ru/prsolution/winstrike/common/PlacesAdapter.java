package ru.prsolution.winstrike.common;
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

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;

public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  List<OrderModel> payList;

  public PlacesAdapter(Context context, List<OrderModel> payList) {
    this.context = context;
    this.payList = payList;
  }


  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    RecyclerView.ViewHolder rvHolder;

    view = LayoutInflater.from(context).inflate(R.layout.item_paid, parent, false);
    rvHolder = new PayViewHolder(view);

    return rvHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    initPayFill(position, (PayViewHolder) holder);
  }

  private void initPayFill(int position, PayViewHolder holder) {
    OrderModel pay = payList.get(position);

    holder.date.setText(pay.getDate());
    holder.time.setText(pay.getTime());
    holder.pc.setText(pay.getPcName());
    holder.pcCode.setText(pay.getAccessCode());

    Glide.with(context)
        .load(pay.getThumbnail())
        .into(holder.thumbnail);

  }

  @Override
  public int getItemCount() {
    return payList.size();
  }

  public class PayViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.seat_title)
    TextView title;
    @BindView(R.id.tv_date)
    TextView date;
    @BindView(R.id.tv_time)
    TextView time;
    @BindView(R.id.tv_pc)
    TextView pc;
    @BindView(R.id.tv_pccode)
    TextView pcCode;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;

    public PayViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

  }

  public class PayEmptyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.thumbnail)
    ImageView thumbnail;

    public PayEmptyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

  }

}
