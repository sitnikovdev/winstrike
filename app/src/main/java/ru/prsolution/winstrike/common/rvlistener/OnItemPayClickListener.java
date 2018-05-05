package ru.prsolution.winstrike.common.rvlistener;
/*
 * Created by oleg on 31.01.2018.
 */

import ru.prsolution.winstrike.common.rvadapter.PayAdapter;

public interface OnItemPayClickListener {
   void onItemPayClick(PayAdapter.PayViewHolder view, Integer position);
}
