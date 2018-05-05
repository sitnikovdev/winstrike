package ru.prsolution.winstrike.common.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.rvadapter.GameAdapter;
import ru.prsolution.winstrike.common.rvlistener.OnItemGameClickListener;
import ru.prsolution.winstrike.common.entity.GameModel;
import ru.prsolution.winstrike.common.utils.TinyDB;

/*
 * Created by oleg on 11.02.2018.
 */

public class DelGameFragment extends Fragment implements OnItemGameClickListener {
    private List<GameModel> mGameList = new ArrayList<>();
    private List<GameModel> mGameFavList = new ArrayList<>();
    private ArrayList<String> mGameNameList = new ArrayList<>();
    private final String SCREEN_ADD = "add";
    private TinyDB tinyDB;

    @BindView(R.id.v_btn_add)
    View addButton;
    @BindView(R.id.tv_btn_title)
    TextView btn_title;

    @BindView(R.id.root_fav)
    ConstraintLayout root;

    @BindView(R.id.rv_games)
    RecyclerView rv_games;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_delgames, container, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getContext());

        if (savedInstanceState == null) {
            mGameList = tinyDB.getListGames("fav_games", GameModel.class);
        }


        rv_games.setVisibility(View.VISIBLE);
        rv_games.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        GameAdapter adapter = new GameAdapter(getActivity(), mGameList);
        adapter.setItemGameClickListener(this);
        rv_games.setAdapter(adapter);

        //DEL screen:
        btn_title.setText("Добавить игры");
        addButton.setOnClickListener(
                it -> {
                    tinyDB.clear();
                    tinyDB.putListGames("fav_games_bak",mGameList);

                    Log.d("gamesLog", "DEL: favorite list: " + tinyDB.getListGames("fav_games",GameModel.class).toString());

                    Intent intent = new Intent(getActivity(), getActivity().getClass());
                    intent.putExtra("screen", SCREEN_ADD);
                    startActivity(intent);
                }
        );

        return view;
    }

    public static Fragment newInstance() {
        return new DelGameFragment();
    }


    @Override
    public void onItemGameClick(GameAdapter.GameViewHolder holder, Integer position) {
        Log.d("myLog", "Game item click is fire");
        GameAdapter.SELECTED_ITEM = position;
        GameModel game = mGameList.get(position);
        if (!game.isFavorite() && !mGameFavList.contains(game)) {
            holder.iv_checked.setVisibility(View.VISIBLE);
            game.setFavared(true);
            mGameFavList.add(game);
        } else if (game.isFavorite() && mGameFavList.contains(game)) {
            holder.iv_checked.setVisibility(View.GONE);
            game.setFavared(false);
            mGameFavList.remove(game);
        }
        rv_games.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onItemGameRemoveClick(GameAdapter.GameViewHolder view, Integer position) {
        mGameList.remove(mGameList.get(position));
        rv_games.removeViewAt(position);
        rv_games.getAdapter().notifyItemRemoved(position);
        rv_games.getAdapter().notifyItemRangeChanged(position, mGameList.size());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("toolbar", "DEL FRAGMENT: On option item selected");
        return super.onOptionsItemSelected(item);
    }

}
