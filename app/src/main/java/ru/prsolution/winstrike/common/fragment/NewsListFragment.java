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
import ru.prsolution.winstrike.common.rvadapter.NewsAdapter;
import ru.prsolution.winstrike.common.rvlistener.OnItemNewsClickListener;
import ru.prsolution.winstrike.common.entity.NewsModel;


public class NewsListFragment extends Fragment  {
    private List<NewsModel> mNewsList = new ArrayList<>();
    @BindView(R.id.rv_news)
    RecyclerView rv_news;


    public NewsListFragment() {
    }

    public static NewsListFragment newInstance(int index) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmt_news, container, false);
        ButterKnife.bind(this, rootView);


        rv_news.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        NewsAdapter adapter = new NewsAdapter(getContext(), mNewsList, (OnItemNewsClickListener) getActivity());
        rv_news.setAdapter(adapter);

        return rootView;
    }


}


