package ru.prsolution.winstrike.common.rvlistener;
/*
 * Created by oleg on 31.01.2018.
 */

import ru.prsolution.winstrike.common.rvadapter.PlacesAdapter;

public interface OnItemPayClickListener {
   void onItemPayClick(PlacesAdapter.PayViewHolder view, Integer position);
}
