package ru.prsolution.winstrike.common.rvlistener;
/*
 * Created by oleg on 31.01.2018.
 */

import ru.prsolution.winstrike.common.rvadapter.BonusAdapter;

public interface OnItemBonusClickListener {
   void onItemBonusClick(BonusAdapter.BonusViewHolder view, Integer position);
}
