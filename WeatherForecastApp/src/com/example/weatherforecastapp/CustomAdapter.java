package com.example.weatherforecastapp;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<WeatherState> rowItem;

    CustomAdapter(Context context, List<WeatherState> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_row, null);
        }

        ImageView weatherTypeImg = (ImageView) convertView.findViewById(R.id.weatherTypeImg);
        TextView cityLbl = (TextView) convertView.findViewById(R.id.cityLbl);
        TextView tempLbl = (TextView) convertView.findViewById(R.id.tempLbl);

        WeatherState rowPos = rowItem.get(position);

        weatherTypeImg.setImageResource(Misc.getWeatherIcon(rowPos.weatherType));
        cityLbl.setText(rowPos.city);
        tempLbl.setText(rowPos.temp + "\u2103");

        return convertView;
    }

}