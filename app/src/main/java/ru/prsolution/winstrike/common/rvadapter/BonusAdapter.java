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

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.BonusModel;
import ru.prsolution.winstrike.common.rvlistener.OnItemBonusClickListener;

public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.BonusViewHolder> {
    private OnItemBonusClickListener itemBonusClickListener;
    public static Integer SELECTED_ITEM = 0;
    private Context context;
    List<BonusModel> bonusList;

    public BonusAdapter(Context context, List<BonusModel> bonusList,OnItemBonusClickListener itemBonusClickListener) {
        this.itemBonusClickListener = itemBonusClickListener;
        this.context = context;
        this.bonusList = bonusList;
        fillBonusList();
    }


    @Override
    public BonusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bonus, parent, false);
        return new BonusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BonusViewHolder holder, int position) {
        BonusModel bonus = bonusList.get(position);
        holder.header.setText(bonus.getHeadTitle());

        holder.header.setText(bonus.getHeadTitle());
        holder.time.setText( bonus.getTime());
        holder.bonus.setText(bonus.getBonus());


        Glide.with(context).load(bonus.getThumbnail()).into(holder.thumbnail);

/*        holder.overflow.setOnClickListener (
//           it ->  context.startActivity(new Intent(context, BonusDetailActivity.class))
          it -> itemBonusClickListener.onItemBonusClick(holder, position)
        );*/
    }

    @Override
    public int getItemCount() {
        return bonusList.size();
    }


    public class BonusViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.tv_date_news)
        TextView header;
        @BindView(R.id.iv_thumbnail)
         ImageView thumbnail;
        @BindView(R.id.tv_time_left)
        TextView time;
        @BindView(R.id.tv_bonus)
        TextView bonus;

        public BonusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void fillBonusList() {
        int[] covers = {R.drawable.img_club, R.drawable.img_club};
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));
        bonusList.add(new BonusModel("Компьютерный клуб «Cybercafe»", covers[0],"В клубе: 7 часов","Бонус 70 рублей"));

    }
}
