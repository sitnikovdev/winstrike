package ru.prsolution.winstrike.common.rvlistener;
/*
 * Created by oleg on 31.01.2018.
 */

import ru.prsolution.winstrike.common.rvadapter.NewsAdapter;

public interface OnItemNewsClickListener {
   void onItemNewsClick(NewsAdapter.NewsViewHolder view, Integer position);
}
