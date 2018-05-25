package ru.prsolution.winstrike.ui.main;
/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.common.ChooseSeatLinearLayout;
import ru.prsolution.winstrike.ui.data.SeatModel;

public class CarouselSeatFragment extends Fragment {


    OnChoosePlaceButtonsClickListener listener;
    private View itemSeat;


    public interface OnChoosePlaceButtonsClickListener {
        void onChooseSeatClick(SeatModel seat);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChoosePlaceButtonsClickListener) {
            listener = (OnChoosePlaceButtonsClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implements OnChoosePlaceButtonsClickListener ");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        itemSeat = inflater.inflate(R.layout.item_seats, container, false);

        int pos = this.getArguments().getInt("pos");
        SeatModel seat = setUpFragmentData(pos);


        TextView seat_title = itemSeat.findViewById(R.id.seat_title);
        seat_title.setText(seat.getType());
        ImageView thumbnail = itemSeat.findViewById(R.id.content);
//        Picasso.with(getContext()).load(seatApi.getThumbnail()).into(thumbnail);
        thumbnail.setImageResource(seat.getThumbnail());

        ChooseSeatLinearLayout root = itemSeat.findViewById(R.id.root);
        Float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        thumbnail.setOnClickListener(
                it -> listener.onChooseSeatClick(seat)
        );

        return itemSeat;

    }

    public SeatModel setUpFragmentData(int pos) {

        if (pos == 0) {
            return new SeatModel("Основной зал", R.drawable.event_room);
        } else {
            return new SeatModel("VIP Room", R.drawable.vip_room);
        }

    }

    public static Fragment newInstance(Context c, int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        return Fragment.instantiate(c, CarouselSeatFragment.class.getName(), bundle);
    }

}

