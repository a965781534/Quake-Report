package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
        magView.setText(currentEarthQuakes.getMagnitude());

        TextView placeView = (TextView) listItemView.findViewById(R.id.place);
        placeView.setText(currentEarthQuakes.getLocation());

        Date dateObject = new Date(currentEarthQuakes.getTimeInMilliseconds());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView) listItemView.findViewById(R.id.times);

        String formattedTime = formateTime(dateObject);
        timeView.setText(formattedTime);

        return listItemView;
    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formateTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
