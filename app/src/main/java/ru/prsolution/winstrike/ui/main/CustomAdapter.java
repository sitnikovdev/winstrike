package ru.prsolution.winstrike.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import ru.prsolution.winstrike.R;

public class CustomAdapter extends ArrayAdapter<Planet> {
  LayoutInflater flater;
  Context mContext;

  public CustomAdapter(Activity context,int resouceId, int textviewId, List<Planet> list){

    super(context,resouceId,textviewId, list);
    flater = context.getLayoutInflater();
    mContext = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    TypedArray typedArrayPlanets = null;
    typedArrayPlanets = mContext.getResources().obtainTypedArray(R.array.planetsInSolarSystem);
//    Planet rowItem = getItem(position);
    Planet rowItem = loadPlanet(mContext,typedArrayPlanets.getResourceId(position, 0));

    View rowview = flater.inflate(R.layout.listitems_layout,null,true);

    TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
    txtTitle.setText(typedArrayPlanets.getResourceId(position, 0));

//    ImageView imageView = (ImageView) rowview.findViewById(R.id.icon);
//    imageView.setImageResource(rowItem.getImageId());

    return rowview;
  }


  private Planet loadPlanet(Context context, int resourceId) {
    Planet planet = null;

    if (context != null) {
      TypedArray typedArrayPlanet = null;
      try {
        String name = null;
        float distance = 0;
        typedArrayPlanet = context.getResources().obtainTypedArray(resourceId);
        for (int i = 0; i < typedArrayPlanet.length(); i++) {
          if (i == 0) {
            name = typedArrayPlanet.getString(i);
          } else if (i == 1) {
            distance = Float.valueOf(typedArrayPlanet.getString(i));
          }
        }
        if (name != null && distance > 0) {
          planet = new Planet(name, distance);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (typedArrayPlanet != null) {
          typedArrayPlanet.recycle();
        }
      }
    }

    return planet;
  }

}
