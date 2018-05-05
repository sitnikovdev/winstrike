package ru.prsolution.winstrike.common.rvlistener;
/*
 * Created by oleg on 31.01.2018.
 */

import ru.prsolution.winstrike.common.rvadapter.GameAdapter;

public interface OnItemGameClickListener {
   void onItemGameClick(GameAdapter.GameViewHolder view, Integer position);
   void onItemGameRemoveClick(GameAdapter.GameViewHolder view, Integer position);
}
