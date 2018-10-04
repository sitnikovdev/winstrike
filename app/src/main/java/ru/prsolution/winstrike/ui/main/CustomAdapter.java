package ru.prsolution.winstrike.ui.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import ru.prsolution.winstrike.R;

public class CustomAdapter extends ArrayAdapter<RowItem> {

  LayoutInflater flater;
  Context mContext;

  public CustomAdapter(Activity context, int resouceId, int textviewId, List<RowItem> list) {

    super(context, resouceId, textviewId, list);
    flater = context.getLayoutInflater();
    mContext = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    RowItem rowItem = getItem(position);

    View rowview = flater.inflate(R.layout.listitems_layout, null, true);

    TextView txtTitle = rowview.findViewById(R.id.title);
    txtTitle.setText(rowItem.getTitle());

    TextView txtAddress = rowview.findViewById(R.id.address);
    txtAddress.setText(rowItem.getAddress());

    return rowview;
  }

}
