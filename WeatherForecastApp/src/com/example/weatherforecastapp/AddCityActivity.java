package com.example.weatherforecastapp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddCityActivity extends Activity {
	
	Map<String, String> searchResults = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_add);	
	}
	
    public void onClickSearchBtn(View v)
    {
    	((LinearLayout) findViewById(R.id.searchcontainer)).removeAllViews();
    	searchResults.clear();
    	new RequestSearchTask().execute(((EditText)findViewById(R.id.cityName)).getText().toString());
    } 
    
    class RequestSearchTask extends AsyncTask<String, String, String>{

        protected String doInBackground(String... params) {
        	URL url = null;
        	
        	try {
    			url = new URL("http://api.openweathermap.org/data/2.5/find?q=" + params[0] + "&type=like&mode=json");
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
    		}
        	
            try {
            	JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader(url.openStream()));
                JsonObject rootobj = root.getAsJsonObject();
            	JsonArray list = rootobj.getAsJsonArray("list");
            	
            	for(JsonElement city : list){
            		
            		searchResults.put(city.getAsJsonObject().get("id").getAsString(), city.getAsJsonObject().get("name").getAsString());

            	}       
            	
            } catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            return "";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(searchResults.size()!=0){

	            for(Entry<String, String> entry : searchResults.entrySet()) {
	                final String key = entry.getKey();
	                final String value = entry.getValue();
	                
	                Log.w(key,value);
	                
	                LinearLayout ll = new LinearLayout(getApplicationContext());
	                ll.setPadding(10, 10, 10, 10);
	                ll.setGravity(Gravity.CENTER_VERTICAL);
		        	ll.setOrientation(0);
	
		        	TextView cityTV = new TextView(getApplicationContext());
		        	cityTV.setText(entry.getValue());
		        	ll.addView(cityTV);
		        	
		        	ImageView addImg = new ImageView(getApplicationContext());
		        	addImg.setPadding(10, 10, 10, 10);
		        	addImg.setImageResource(R.drawable.add);
		        	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( 50, 50);
		        	addImg.setLayoutParams(layoutParams);
		        	addImg.setOnClickListener(new OnClickListener() {
		        		@Override
		        		public void onClick(View view) {
		        			DBHelper dbHelper = new DBHelper(getApplicationContext());
		        			SQLiteDatabase db = dbHelper.getWritableDatabase();
		        			
		        			Cursor c = db.rawQuery("SELECT * FROM CITIES WHERE Identificator = '" + key + "';", null);
		                	
		                    if (!c.moveToFirst()) {
		        			
			        			ContentValues cv = new ContentValues();
			            	    
			            	    cv.put("Identificator", key);
			            	    cv.put("Name", value);
			            	    
			            	    db.insert("CITIES", null, cv);
			            	    
			            	    dbHelper.close();
			            	    
			            	    Intent returnIntent = new Intent();
			            	    returnIntent.putExtra("result","Ok");
			            	    setResult(RESULT_OK,returnIntent);
			            	    finish();
		                    }else{
		                    	Toast.makeText(getApplicationContext(), "City is already in the list", Toast.LENGTH_SHORT).show();
		                    }
		        		}
		        	});	        	
		        	
		        	ll.addView(addImg);
		        	
		        	((LinearLayout) findViewById(R.id.searchcontainer)).addView(ll);
	
	            }
        	
            }else{
            	Toast.makeText(getApplicationContext(), "City was not found", Toast.LENGTH_SHORT).show();
            }
        }
    }    
}