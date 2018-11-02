package ru.prsolution.winstrike.common.vpadapter;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.BR;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;


public final class SeatAdapter extends ViewModelAdapter {

    @Override
    public void reload(@Nullable SwipeRefreshLayout refreshLayout) {

    }

    public SeatAdapter(List<OrderModel> orders) {
        registerCell(OrdersViewModel.class, R.layout.item_paid, BR.vm);
        for (OrderModel order : orders) {
            OrdersViewModel vm = new OrdersViewModel();
            vm.setArenaName(order.getArenaName());
            vm.setDate(order.getDate());
            vm.setTime(order.getTime());
            vm.setPlaceName(order.getPcName());
            vm.setAccessCode(order.getAccessCode());
            mItems.add(vm);
        }
    }

}
