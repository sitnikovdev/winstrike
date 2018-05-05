package ru.prsolution.winstrike.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

public class AddGameFragment extends Fragment implements OnItemGameClickListener {
    private List<GameModel> mGameList = new ArrayList<>();
    private List<GameModel> mGameCheckedList = new ArrayList<>();
    private List<String> mGameNameList = new ArrayList<>();
    private List<GameModel> mGameFavList = new ArrayList<>();
    private List<GameModel> mGameTempList = new ArrayList<>();
    private final String SCREEN_DEL = "del";
    private final String SCREEN_ADD = "add";
    private TinyDB tinyDB;
    boolean setChecked = true;

    @BindView(R.id.v_btn_add)
    View addButton;
    @BindView(R.id.tv_btn_title)
    TextView btn_title;

    @BindView(R.id.root_fav)
    ConstraintLayout root;

    @BindView(R.id.rv_games)
    RecyclerView rv_games;

    private void loadGames(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mGameNameList.clear();
        int size = preferences.getInt("games_size", 0);
        for (int i = 0; i < size; i++) {
            mGameNameList.add(preferences.getString("games_" + i, null));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_add_games, container, false);
        ButterKnife.bind(this, view);
        tinyDB = new TinyDB(getContext());

        loadGames(getContext());

        Log.d("toolbar", " Game list " + mGameNameList.toString());

        mGameCheckedList = tinyDB.getListGames("fav_games_bak", GameModel.class);
        fillGameList();
        Log.d("toolbar", "fav_games_bak" + mGameCheckedList.toString());
        for (GameModel gameChk : mGameCheckedList) {
            for (GameModel game : mGameList) {
                if (game.getName().equals(gameChk.getName())) {
                    game.setFavared(true);
//                    Log.d("gamesLog", "game is checked: " + game.getPlaceName());
                }
            }
        }

        rv_games.setVisibility(View.VISIBLE);
        rv_games.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        GameAdapter adapter = new GameAdapter(getActivity(), mGameList);
        adapter.setItemGameClickListener(this);
        rv_games.setAdapter(adapter);


        // ADD screen:
        btn_title.setText("Применить");
        addButton.setOnClickListener(
                it -> {
                    Log.d("gamesLog", "ADD-bak: favorite list: " + tinyDB.getListGames("fav_games_bak", GameModel.class).toString());
                    Log.d("gamesLog", "ADD-fav: favorite list: " + tinyDB.getListGames("fav_games", GameModel.class).toString());
                    for (GameModel game : mGameFavList) {
                        game.setType(2);
                    }
                    mGameTempList = tinyDB.getListGames("fav_games_bak", GameModel.class);
                    mGameFavList.addAll(mGameTempList);
                    tinyDB.clear();
                    tinyDB.putListGames("fav_games", mGameFavList);

                    for (GameModel game : mGameFavList) {
                        mGameNameList.add(game.getName());
                    }

                    saveFavGames();

                    Intent intent = new Intent(getActivity(), getActivity().getClass());
                    intent.putExtra("screen", SCREEN_DEL);
                    startActivity(intent);
                }
        );

        return view;
    }

    public static Fragment newInstance() {
        return new AddGameFragment();
    }

    private boolean saveFavGames() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor mEdit = preferences.edit();
        mEdit.putInt("games_size", mGameNameList.size());
        for (int i = 0; i < mGameList.size(); i++) {
            mEdit.remove("games_" + i);
            mEdit.putString("games_" + i, mGameList.get(i).getName());
//            Log.d("toolbar", "games is saved: " + mGameList.get(i).getPlaceName());
        }
        return mEdit.commit();
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

    }

    private void fillGameList() {
        mGameList.add(new GameModel("Call of Duty", false));
        mGameList.add(new GameModel("Assassin’s Creed: Origins", false));
        mGameList.add(new GameModel("CS:GO", false));
        mGameList.add(new GameModel("Wolfenstein 2: The New Colossus", false));
    }

}
