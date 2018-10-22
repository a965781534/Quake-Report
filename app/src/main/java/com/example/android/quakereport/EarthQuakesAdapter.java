package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class EarthQuakesAdapter extends ArrayAdapter<EarthQuakes> {

    public EarthQuakesAdapter(Context context, ArrayList<EarthQuakes> arrayList) {
        super(context, 0, arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        final EarthQuakes currentEarthQuakes = getItem(position);

        TextView magView = (TextView) listItemView.findViewById(R.id.mag);
        magView.setText(currentEarthQuakes.getMag());

        TextView placeView = (TextView) listItemView.findViewById(R.id.place);
        placeView.setText(currentEarthQuakes.getPlace());

        TextView timeView = (TextView) listItemView.findViewById(R.id.times);
        timeView.setText(currentEarthQuakes.getTime());

        return listItemView;
    }
}
