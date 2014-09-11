package com.example.weatherforecastapp;


import java.text.SimpleDateFormat;
import java.util.Date;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.util.Log;

public class CityDetailFragment extends Fragment {
	
  private WeatherState course;

    public static final String ARG_ITEM_ID = "item_id";


    public CityDetailFragment() {
    }
    
    //ArrayList<WeatherState>

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("myApp", "Detail Fragment: onCreate");

        if (getArguments().containsKey("course")) {
            course = getArguments().getParcelable("course");
      	  Log.v("myApp", "Detail Fragment: Retrieved course from argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	Log.v("myApp", "Detail Fragment: R.Layout.fragment_golfcourse_detail: " + R.layout.fragment_city_detail);
        View rootView = inflater.inflate(R.layout.fragment_city_detail, container, false);
        if (course != null) {
        	((ScrollView) rootView.findViewById(R.id.scroll)).setVisibility(View.VISIBLE);
        	((TextView) rootView.findViewById(R.id.msg)).setVisibility(View.INVISIBLE);
            ((TextView) rootView.findViewById(R.id.cityName)).setText(course.city);
            ((TextView) rootView.findViewById(R.id.temp)).setText(course.temp + "\u2103");
            ((ImageView) rootView.findViewById(R.id.weatherImg)).setImageResource(Misc.getWeatherIcon(course.weatherType));
            ((TextView) rootView.findViewById(R.id.description)).setText(course.weatherDescription);
            ((TextView) rootView.findViewById(R.id.humidity)).setText("Humidity: " + course.humidity);
            ((TextView) rootView.findViewById(R.id.pressure)).setText("Pressure: " + course.pressure);
            ((TextView) rootView.findViewById(R.id.wspeed)).setText("Wind speed: " + course.windSpeed);
            ((TextView) rootView.findViewById(R.id.wdirect)).setText("Wind direction: " + Misc.getWindDirection(Double.valueOf(course.windDirect)));
        }
        else {
        	((ScrollView) rootView.findViewById(R.id.scroll)).setVisibility(View.GONE);
        	((TextView) rootView.findViewById(R.id.msg)).setVisibility(View.VISIBLE);
        }
        if (course != null) {
        	
        	DBHelper dbHelper = new DBHelper(getActivity());
        	SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM FORECAST_WEATHER_DATA WHERE City_Id = '" + course.cityId + "' ORDER BY Day ASC;", null);
        	
            if (c.moveToFirst()) {

              int dateColIndex = c.getColumnIndex("Day");
              int dayColIndex = c.getColumnIndex("Day_Temp");
              int nightColIndex = c.getColumnIndex("Night_Temp");
              int typeColIndex = c.getColumnIndex("Weather_Type");

              do {
            	  
  	        	LinearLayout ll = new LinearLayout(getActivity());
  	        	ll.setOrientation(1);
  	        	ll.setGravity(Gravity.CENTER_HORIZONTAL);
  	        	ll.setPadding(5, 5, 5, 5);
  	        	
  	        	Date date = new Date ();
  	        	date.setTime(Long.valueOf(c.getString(dateColIndex))*1000);
  	        	
  	        	TextView dateTV = new TextView(getActivity());
  	        	dateTV.setText(new SimpleDateFormat("dd.MM").format(date));
  	        	dateTV.setGravity(Gravity.CENTER_HORIZONTAL);
  	        	ll.addView(dateTV);
  	        	
  	        	ImageView typeImg = new ImageView(getActivity());
  	        	typeImg.setImageResource(Misc.getWeatherIcon(c.getString(typeColIndex)));
  	        	ll.addView(typeImg);
  	        	
  	        	TextView tempTV = new TextView(getActivity());
  	        	tempTV.setText("Day: " + c.getString(dayColIndex) + "\u2103" + "\n" + "Night: " + c.getString(nightColIndex) + "\u2103");
  	        	tempTV.setGravity(Gravity.CENTER_HORIZONTAL);
  	        	ll.addView(tempTV);
  	        	
  	        	((LinearLayout) rootView.findViewById(R.id.forecastContainer)).addView(ll);
              } while (c.moveToNext());
            } else
            c.close();
            dbHelper.close();
        	
        	//http://api.openweathermap.org/data/2.5/forecast/daily?id=524901,498817&mode=json&units=metric&cnt=7
        }
        else {
        //((TextView) rootView.findViewById(R.id.city_detail)).setText("Welcome to Golf Droid");
        }
        return rootView;
    }
    


@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.v("myApp", "Detail Fragment: Ready to save details fragment to outState, Id = " + getId());
//    outState.putParcelable("course", course);
	}

}
