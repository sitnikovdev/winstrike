package ru.prsolution.winstrike.common.fragment;

import android.os.Bundle;
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
import ru.prsolution.winstrike.common.entity.GameModel;
import ru.prsolution.winstrike.common.rvadapter.GameAdapter;
import ru.prsolution.winstrike.common.rvlistener.OnItemGameClickListener;
import ru.prsolution.winstrike.common.utils.TinyDB;
import timber.log.Timber;

/*
 * Created by oleg on 11.02.2018.
 */

public class StartGameFragment extends Fragment implements OnItemGameClickListener {
    private final  String SCREEN_ADD = "add";
    private List<GameModel> mGameCheckedList = new ArrayList<>();

    @BindView(R.id.v_btn_add)
    View addButton;
    @BindView(R.id.tv_btn_title)
    TextView btn_title;

    @BindView(R.id.root_fav)
    ConstraintLayout root;

    @BindView(R.id.rv_games)
    RecyclerView rv_games;

    @Override
    public void onPause() {
        super.onPause();
        Log.d("cycle", "Start on Pause.");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("cycle", "Start on Stop.");
        Log.d("cycle", "");
        Log.d("cycle", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("cycle", "Start on Destroy.");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("cycle", "");
        Log.d("cycle", "");
        Log.d("cycle", "Start on Start.");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("cycle", "Start on Resume.");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("cycle", "Start on Create.");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_add_games, container, false);
        ButterKnife.bind(this, view);
        TinyDB tinyDB = new TinyDB(getContext());



        mGameCheckedList = tinyDB.getListGames("fav_games", GameModel.class);
        if (mGameCheckedList.isEmpty()){
            mGameCheckedList = tinyDB.getListGames("fav_games_bak", GameModel.class);
        }
        if (mGameCheckedList.isEmpty()) {
            mGameCheckedList.add(new GameModel("Call of Duty", true));
        }
        ArrayList<GameModel> mGameCheckedList2 = tinyDB.getListGames("fav_games_bak", GameModel.class);
        Timber.d("Profile: fav_games" + mGameCheckedList.toString());
        Timber.d("Profile: fav_games_bak" + mGameCheckedList2.toString());

        for (GameModel game : mGameCheckedList) {
            game.setType(1);
        }

        rv_games.setVisibility(View.VISIBLE);
        rv_games.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        Log.d("game", " mGameCheckedList: " + mGameCheckedList2.toString());
        GameAdapter adapter = new GameAdapter(getActivity(), mGameCheckedList);
        adapter.setItemGameClickListener(this);
        rv_games.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        addButton.setOnClickListener(
                it -> {
                    setBtnEnable(addButton,false);
/*                        Intent intent = new Intent(getActivity(),getActivity().getClass());
                        intent.putExtra("screen", SCREEN_ADD);
                        startActivity(intent);*/
                }
        );

        return view;

    }

    private void setBtnEnable(View v, Boolean isEnable) {
        if (isEnable) {
            v.setAlpha(1f);
            v.setClickable(true);
        } else {
            v.setAlpha(.5f);
            v.setClickable(false);
        }
    }

    public static Fragment newInstance() {
        return new StartGameFragment();
    }

    @Override
    public void onItemGameClick(GameAdapter.GameViewHolder holder, Integer position) {
    }

    @Override
    public void onItemGameRemoveClick(GameAdapter.GameViewHolder view, Integer position) {

    }


}
