package ru.prsolution.winstrike.ui.main;
/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.ChooseSeatLinearLayout;
import ru.prsolution.winstrike.mvp.apimodels.Room;
import ru.prsolution.winstrike.mvp.models.SeatModel;

public class CarouselSeatFragment extends Fragment {


  OnChoosePlaceButtonsClickListener listener;
  private View itemSeat;
  private static final String ACTIVE_ARENA = "extra_number";
  private int mPosition;
  private List<Room> rooms;
  private MainScreenActivity mainScreenActivity;


  public interface OnChoosePlaceButtonsClickListener {

    void onChooseArenaSeatClick(SeatModel seat);
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


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    this.mainScreenActivity = (MainScreenActivity) getActivity();
    if (container == null) {
      return null;
    }
    itemSeat = inflater.inflate(R.layout.item_seats, container, false);

    this.mPosition = mainScreenActivity.selectedArena;
    this.rooms = mainScreenActivity.rooms;
    SeatModel seat = setUpFragmentData(mPosition);

    TextView seat_title = itemSeat.findViewById(R.id.seat_title);
    seat_title.setText(seat.getType());
    SimpleDraweeView thumbnail = itemSeat.findViewById(R.id.content);
    Uri uri = Uri.parse(rooms.get(this.mPosition).getImageUrl());
    thumbnail.setImageURI(uri);

    ChooseSeatLinearLayout root = itemSeat.findViewById(R.id.root);
    Float scale = this.getArguments().getFloat("scale");
    root.setScaleBoth(scale);

    thumbnail.setOnClickListener(
        it -> listener.onChooseArenaSeatClick(seat)
    );

    return itemSeat;

  }

  public SeatModel setUpFragmentData(int pos) {

    if (pos == 0) {
      return new SeatModel(getString(R.string.common_hall),
          rooms.get(0).getImageUrl()
          , rooms.get(0).getUsualDescription()
      );
    } else if (pos == 1) {
      return new SeatModel(getString(R.string.vip_hp),
          rooms.get(1).getImageUrl()
          , rooms.get(0).getUsualDescription()
      );
    } else {
      return null;
    }

  }

  public static Fragment newInstance(Context c, int pos) {
    Bundle bundle = new Bundle();
    bundle.putInt("pos", pos);
    return Fragment.instantiate(c, CarouselSeatFragment.class.getName(), bundle);
  }

}

