package ru.prsolution.winstrike.ui.main;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import ru.prsolution.winstrike.R;

public class CustomSpinnAdapter extends ArrayAdapter<RowItem> {

  LayoutInflater flater;

/*  interface OnItemClickListener {

    void itemSelect(View v);
  }*/

//  OnItemClickListener listener;
  Context mContext;
  int selectedItem;

  public CustomSpinnAdapter(Activity context, int resouceId, int textviewId, List<RowItem> list) {

    super(context, resouceId, textviewId, list);
//    this.listener = listener;
    this.mContext = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    return rowview(convertView, position);
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    return rowview(convertView, position);
  }

  private View rowview(View convertView, int position) {

    RowItem rowItem = getItem(position);

    viewHolder holder;
    View rowview = convertView;



    if (rowview == null) {

      holder = new viewHolder();
      flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      rowview = flater.inflate(R.layout.item_arena, null, false);

      holder.txtTitle = (TextView) rowview.findViewById(R.id.title);
      holder.txtAddress = (TextView) rowview.findViewById(R.id.address);

      holder.root = rowview.findViewById(R.id.root_item);

      switch (position) {
        case 0:
          holder.txtTitle.setTextColor(mContext.getColor(R.color.color_accent));
          holder.txtAddress.setTextColor(mContext.getColor(R.color.color_accent));
          break;
        case 1:
          holder.txtTitle.setTextColor(mContext.getColor(R.color.color_black));
          holder.txtAddress.setTextColor(mContext.getColor(R.color.color_black));
          break;
        default:
          holder.txtTitle.setTextColor(mContext.getColor(R.color.color_black));
          holder.txtAddress.setTextColor(mContext.getColor(R.color.color_black));
          break;
      }



      rowview.setTag(holder);
    } else {
      holder = (viewHolder) rowview.getTag();
    }
    holder.txtTitle.setText(rowItem.getTitle());
    holder.txtAddress.setText(rowItem.getAddress());

    return rowview;
  }


  private class viewHolder {

    ConstraintLayout root;
    TextView txtTitle;
    TextView txtAddress;
  }
}
