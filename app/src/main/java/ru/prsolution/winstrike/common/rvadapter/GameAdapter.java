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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.GameModel;
import ru.prsolution.winstrike.common.rvlistener.OnItemGameClickListener;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private OnItemGameClickListener itemGameClickListener;
    public static Integer SELECTED_ITEM = 0;
    private Context context;
    List<GameModel> gamesList;
    public final static int CHECK_TYPE = 1;
    public final static int REMOVE_TYPE = 2;

    public GameAdapter(Context context, List<GameModel> gamesList) {
        this.itemGameClickListener = itemGameClickListener;
        this.context = context;
        this.gamesList = gamesList;
    }



    public void setItemGameClickListener(OnItemGameClickListener itemGameClickListener) {
        this.itemGameClickListener = itemGameClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        GameModel game = gamesList.get(position);
        switch (game.getType()) {
            case 1:
                return CHECK_TYPE;
            case 2:
                return REMOVE_TYPE;
            default: {
                return CHECK_TYPE;
            }
        }
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case CHECK_TYPE: {
                view = LayoutInflater.from(context).inflate(R.layout.item_games, parent, false);
                return new GameViewHolder(view);
            }
            case REMOVE_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_games, parent, false);
                return new GameViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_games, parent, false);
                return new GameViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        GameModel games = gamesList.get(position);

        if (games.getType() == 2) {
            holder.iv_remove.setVisibility(View.VISIBLE);
        }else {
            holder.iv_remove.setVisibility(View.GONE);
        }


        switch (holder.getItemViewType()) {
            case CHECK_TYPE: {
                initCheck(position, (GameViewHolder) holder);
                break;
            }
            case REMOVE_TYPE: {
                initRemove(position, (GameViewHolder) holder);
                break;
            }
        }
//        Log.d("myLog", "Game type is " + holder.getItemViewType());
    }

    private void initCheck(int position, GameViewHolder holder) {
        GameModel games = gamesList.get(position);
        holder.name.setText(games.getName());
        holder.iv_checked.setImageResource(R.drawable.checked);
        holder.iv_remove.setImageResource(R.drawable.ic_remove);
        if (games.isFavorite()) {
            holder.iv_checked.setVisibility(View.VISIBLE);
        } else {
            holder.iv_checked.setVisibility(View.GONE);
        }


        holder.name.setOnClickListener(
                it ->itemGameClickListener.onItemGameClick(holder,position)
        );
    }

    private void initRemove(int position, GameViewHolder holder) {
        GameModel games = gamesList.get(position);
        holder.name.setText(games.getName());
        holder.iv_checked.setImageResource(R.drawable.checked);
        holder.iv_remove.setImageResource(R.drawable.ic_remove);
/*        holder.name.setOnClickListener(
                it ->itemGameClickListener.onItemGameClick(holder,position)
        );*/
        holder.iv_remove.setOnClickListener(
                it ->itemGameClickListener.onItemGameRemoveClick(holder,position)
        );
    }






    @Override
    public int getItemCount() {
        return gamesList.size();
    }


public class GameViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.iv_checked)
    public
    ImageView iv_checked;
    @BindView(R.id.iv_remove)
    public
    ImageView iv_remove;

    public GameViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

}
