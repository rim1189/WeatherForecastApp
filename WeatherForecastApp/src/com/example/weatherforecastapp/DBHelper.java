package com.example.weatherforecastapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "weatherdata";
	 
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_CREATE_CITIES = "CREATE TABLE CITIES (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name ntext, Identificator ntext);";
    private static final String DATABASE_CREATE_CUREENT = "CREATE TABLE CURRENT_WEATHER_DATA (Id INTEGER PRIMARY KEY AUTOINCREMENT,City ntext, City_Id ntext, Day ntext, Temperature ntext, Weather_Type ntext, Humidity ntext, Pressure ntext, Weather_Desc ntext, Wind_Speed ntext, Wind_Direct ntext);";
    private static final String DATABASE_CREATE_FORECAST = "CREATE TABLE FORECAST_WEATHER_DATA (Id INTEGER PRIMARY KEY AUTOINCREMENT, City_Id ntext, Day ntext, Day_Temp ntext, Night_Temp ntext, Weather_Type ntext);";
    private static final String DATABASE_ADD_MSK = "INSERT INTO CITIES (Name, Identificator) VALUES ('Moscow', '524901');";
    private static final String DATABASE_ADD_SPB ="INSERT INTO CITIES (Name, Identificator) VALUES ('Saint Petersburg', '498817');";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_CITIES);
        database.execSQL(DATABASE_CREATE_CUREENT);
        database.execSQL(DATABASE_CREATE_FORECAST);
        database.execSQL(DATABASE_ADD_MSK);
        database.execSQL(DATABASE_ADD_SPB);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}
