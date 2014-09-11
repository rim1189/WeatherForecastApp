package com.example.weatherforecastapp;

import android.content.Intent;

import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import android.content.ContentValues;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityListActivity extends FragmentActivity
        implements CityListFragment.Callbacks {

    private boolean mTwoPane;
    DataModel dataModel;
    JsonParser jp;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jp = new JsonParser();

        new RequestWeatherTask().execute("");

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent addIntent = new Intent(this, AddCityActivity.class);
                startActivityForResult(addIntent,1);
                return true;
            case R.id.menu_delete:
                Intent deleteIntent = new Intent(this, DeleteCityActivity.class);
                startActivityForResult(deleteIntent,1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getId() {
    	// TODO Auto-generated method stub
    	return null;
    } 

    class RequestWeatherTask extends AsyncTask<String, String, String>{

        protected String doInBackground(String... uri) {

        	dataModel = new DataModel();

            return "";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setViews();  	
        }
    }
    
    protected void setViews()
    {
    	FragmentManager fm = getSupportFragmentManager();
       	setContentView(R.layout.activity_city_list);
        
        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
            CityDetailFragment df = (CityDetailFragment) fm.findFragmentByTag("Detail");

	        df = new CityDetailFragment();
	        Bundle args = new Bundle();
	        args.putParcelable("course", null);
	        df.setArguments(args);
	        fm.beginTransaction().replace(R.id.city_detail_container, df, "Detail").commit();
        }
        
        CityListFragment cf = (CityListFragment) fm.findFragmentByTag("List");
        cf = new CityListFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("courses", dataModel.getCourses());
        cf.setArguments(arguments);           	
        fm.beginTransaction().replace(R.id.city_list, cf, "List").commit(); 	
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
            	new RequestWeatherTask().execute("");
            }
            if (resultCode == RESULT_CANCELED) {
                
            }
        }
    }

	@Override
	public void onItemSelected(WeatherState c) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("course", c);
            CityDetailFragment fragment = new CityDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.city_detail_container, fragment, "Detail")
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, CityDetailActivity.class);
            detailIntent.putExtra("course", c);
            startActivity(detailIntent);
        }
    }
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	class DataModel {
		
        private ArrayList<WeatherState> states = new ArrayList<WeatherState>();
		
        // Initializer to read a text file into an array of golfcourse objects    
		public DataModel() {
			if(isOnline()){
				String surl = "http://api.openweathermap.org/data/2.5/group?id="; 
				DBHelper dbHelper = new DBHelper(getApplicationContext());
	            SQLiteDatabase db = dbHelper.getWritableDatabase();
	        	
	            Cursor c = db.query("CITIES", null, null, null, null, null, null);
	
	            if (c.moveToFirst()) {
	            	int identColIndex = c.getColumnIndex("Identificator");
	            	do {
	            		surl += c.getString(identColIndex) + ","; 
	            	} while (c.moveToNext());
	            } 
	            
	            c.close();
	            
	            surl += "&units=metric";
	            		
	        	URL url = null;
	        	
	        	try {
	    			url = new URL(surl);
	    		} catch (MalformedURLException e) {
	    			e.printStackTrace();
	    		}
	        	
	            try {
	                jp = new JsonParser();
	                JsonElement root = jp.parse(new InputStreamReader(url.openStream()));
	                JsonObject rootobj = root.getAsJsonObject();
	            	JsonArray list = rootobj.getAsJsonArray("list");
	            	
	            	db.delete("CURRENT_WEATHER_DATA",null, null);
	            	
	            	for(JsonElement city : list){
	            		WeatherState ws = new WeatherState(city.getAsJsonObject().get("id").getAsString());
	            		ws.city = city.getAsJsonObject().get("name").getAsString();
		        		ws.temp = city.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString();
		        		ws.date = city.getAsJsonObject().get("dt").getAsString();
		        		ws.humidity = city.getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsString();
		        		ws.pressure = city.getAsJsonObject().get("main").getAsJsonObject().get("pressure").getAsString();        		
		        		ws.weatherType = city.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
		        		ws.weatherDescription = city.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
		        		ws.windDirect = city.getAsJsonObject().get("wind").getAsJsonObject().get("deg").getAsString();
		        		ws.windSpeed = city.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsString();
		        		
		        		ContentValues cvc = new ContentValues();
		        		cvc.put("City_Id", city.getAsJsonObject().get("id").getAsString());
		        		cvc.put("City", ws.city);
		        		cvc.put("Day", ws.date);
		        		cvc.put("Temperature", ws.temp);
		        		cvc.put("Weather_Type", ws.weatherType);
		        		cvc.put("Humidity", ws.humidity);
		        		cvc.put("Pressure", ws.pressure);
		        		cvc.put("Weather_Desc", ws.weatherDescription);
		        		cvc.put("Wind_Speed", ws.windSpeed);
		        		cvc.put("Wind_Direct", ws.windDirect);
	
		        		db.insert("CURRENT_WEATHER_DATA", null, cvc);
	
		        		states.add(ws);
		        		
		            	try {
		        			url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?id=" + city.getAsJsonObject().get("id").getAsString() + "&mode=json&units=metric&cnt=7");
		        		} catch (MalformedURLException e) {
		        			e.printStackTrace();
		        		}
		        		
		                JsonElement rootf = jp.parse(new InputStreamReader(url.openStream()));
		                JsonObject rootobjf = rootf.getAsJsonObject();
		            	JsonArray forecasts = rootobjf.getAsJsonArray("list");
		            	
		            	db.delete("FORECAST_WEATHER_DATA", "City_Id" + "=?", new String[] { city.getAsJsonObject().get("id").getAsString() });
		            	
		            	for(JsonElement day : forecasts){
		            		
		            	    ContentValues cv = new ContentValues();
		            	    
		            	    String date = day.getAsJsonObject().get("dt").getAsString();
		            	    String dayTemp = day.getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsString();
		            	    String nightTemp = day.getAsJsonObject().get("temp").getAsJsonObject().get("night").getAsString();
		            	    String type = day.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
		            	    
		            	    cv.put("City_Id", city.getAsJsonObject().get("id").getAsString());
		            	    cv.put("Day", date);
		            	    cv.put("Day_Temp", dayTemp);
		            	    cv.put("Night_Temp", nightTemp);
		            	    cv.put("Weather_Type", type);
	
		            	    Log.w("stdf",date);
		            	    Log.w("stdf",dayTemp);
		            	    
		            	    long idn = db.insert("FORECAST_WEATHER_DATA", null, cv);
		            	    Log.w("stdf",String.valueOf(idn));
		            	}       
	            	}       
	            	
	            } catch (IOException e) {
	            	offlineFillStates();
	    		}
	            c.close();
	            dbHelper.close();
			}else{
				offlineFillStates();
			}
		}
		
	    public void offlineFillStates() {
	    	DBHelper dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
        	Cursor c = db.query("CURRENT_WEATHER_DATA", null, null, null, null, null, null);
        	
            if (c.moveToFirst()) {

              int cityIdColIndex = c.getColumnIndex("City_Id");
              int cityColIndex = c.getColumnIndex("City");
              int dayColIndex = c.getColumnIndex("Day");
              int tempColIndex = c.getColumnIndex("Temperature");
              int humColIndex = c.getColumnIndex("Humidity");
              int typeColIndex = c.getColumnIndex("Weather_Type");
              int presColIndex = c.getColumnIndex("Pressure");
              int desctColIndex = c.getColumnIndex("Weather_Desc");
              int speedColIndex = c.getColumnIndex("Wind_Speed");
              int directColIndex = c.getColumnIndex("Wind_Direct");

              do {
            	WeatherState ws = new WeatherState(c.getString(cityIdColIndex));
          		ws.city = c.getString(cityColIndex);
	        		ws.temp = c.getString(tempColIndex);
	        		ws.date = c.getString(dayColIndex);
	        		ws.humidity = c.getString(humColIndex);
	        		ws.pressure = c.getString(presColIndex);        		
	        		ws.weatherType = c.getString(typeColIndex);
	        		ws.weatherDescription = c.getString(desctColIndex);
	        		ws.windDirect = c.getString(directColIndex);
	        		ws.windSpeed = c.getString(speedColIndex);
	        		
	        		states.add(ws);
            	  
              } while (c.moveToNext());
            } else{
            	c = db.query("CITIES", null, null, null, null, null, null);

                if (c.moveToFirst()) {
                	int identColIndex = c.getColumnIndex("Identificator");
                	int nameColIndex = c.getColumnIndex("Name");
                	do {
                		WeatherState ws = new WeatherState(c.getString(identColIndex));
                  		ws.city = c.getString(nameColIndex);
                		states.add(ws); 
                	} while (c.moveToNext());
                } 
            }
	    }
		
		// Method to retrieve courses
	    public ArrayList<WeatherState> getCourses() {
	    	return states;
	    }
	}
}
