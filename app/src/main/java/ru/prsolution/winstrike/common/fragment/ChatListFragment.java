package ru.prsolution.winstrike.common.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.ChatModel;
import ru.prsolution.winstrike.common.rvadapter.ChatAdapter;


public class ChatListFragment extends Fragment {
    List<ChatModel> chatList = new ArrayList<>();
    @BindView(R.id.rv_chat_list)
    RecyclerView rv_chat;


    public ChatListFragment() {
    }

    public static ChatListFragment newInstance(int index) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmt_chat, container, false);
        ButterKnife.bind(this, rootView);

        rv_chat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        fillChatsList();
        ChatAdapter adapter = new ChatAdapter(getContext(), chatList);
        rv_chat.setAdapter(adapter);
        return rootView;
    }

    private void fillChatsList() {
        int[] bubbles = {R.drawable.img_bubble_left, R.drawable.img_big_bubble_right, R.drawable.img_small_bubble_right};
        int[] avatar = {R.drawable.avatar_vasyan};

        chatList.add(new
                ChatModel("Игорь", avatar[0], bubbles[0], "1:57PM", "Отлично поиграли!", 2));
        chatList.add(new ChatModel("Васян", avatar[0], bubbles[1], "1:58PM", "Кто будет в Москве в арене Win Strike?", 1));
        chatList.add(new ChatModel("Васян", avatar[0], bubbles[2], "1:58PM", "Поиграть в Dota2?", 1));
        chatList.add(new ChatModel("Игорь", avatar[0], bubbles[0], "1:57PM", "Отлично поиграли!", 2));
        chatList.add(new
                ChatModel("Васян", avatar[0], bubbles[1], "1:58PM", "Кто будет в Москве в арене Win Strike?", 1));
        chatList.add(new
                ChatModel("Васян", avatar[0], bubbles[2], "1:58PM", "Поиграть в Dota2?", 1));
        chatList.add(new
                ChatModel("Игорь", avatar[0], bubbles[0], "1:57PM", "Отлично поиграли!", 2));
    }

}


