package com.example.weatherforecastapp;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DeleteCityActivity extends ListActivity  {
    
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_delete);
        
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    	
        Cursor c = db.query("CITIES", null, null, null, null, null, null);

        if (c.moveToFirst()) {
        	int identColIndex = c.getColumnIndex("Identificator");
        	int nameColIndex = c.getColumnIndex("Name");
        	do {
        		keys.add(c.getString(identColIndex));
        		values.add(c.getString(nameColIndex));
        	} while (c.moveToNext());
        } 
        
        c.close();
        dbHelper.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values );

        setListAdapter(adapter); 
   }

    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	        
    	super.onListItemClick(l, v, position, id);
	
	           final int itemPosition = position;

	           AlertDialog.Builder builder = new AlertDialog.Builder(this);
	       		builder.setTitle("Delete")
	       			.setMessage("Are you sure?")
	       			.setCancelable(true)
	       			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
   						public void onClick(DialogInterface dialog, int id) {
   							DBHelper dbHelper = new DBHelper(getApplicationContext());
   					        SQLiteDatabase db = dbHelper.getWritableDatabase();
   							db.delete("CITIES", "Identificator" + "=?", new String[] { keys.get(itemPosition) });
   			            	
   							Intent returnIntent = new Intent();
		            	    returnIntent.putExtra("result","Ok");
		            	    setResult(RESULT_OK,returnIntent);
		            	    finish();
   						}
   					})
	       			.setNegativeButton("Cancel",
	       					new DialogInterface.OnClickListener() {
	       						public void onClick(DialogInterface dialog, int id) {
	       							dialog.cancel();
	       						}
	       					});
	       	AlertDialog alert = builder.create();
	       	alert.show();
	  }
}