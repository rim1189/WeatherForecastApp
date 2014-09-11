package com.example.weatherforecastapp;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherState implements Parcelable {
	String	city = "None";
	String	cityId = "None";
	String	date = "None";
	String	temp = "None";
	String	windSpeed = "None";
	String	windDirect = "0";
	String	pressure = "None";
	String	humidity = "None";
	String	weatherType = "None";
	String	weatherDescription = "None";
	long sunset = 0;
	long sunrise = 0;
	
	WeatherState(String cityId) {
		this.cityId = cityId;
	}
	
	public String toString () {
		return city;
	}
	
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(cityId);
        out.writeString(city);
        out.writeString(temp);
        out.writeString(windDirect);
        out.writeString(date);
        out.writeString(windSpeed);
        out.writeString(pressure);
        out.writeString(humidity);
        out.writeString(weatherType);
        out.writeString(weatherDescription);
    }

    public static final Parcelable.Creator<WeatherState> CREATOR
            = new Parcelable.Creator<WeatherState>() {
        public WeatherState createFromParcel(Parcel in) {
            return new WeatherState(in);
        }

        public WeatherState[] newArray(int size) {
            return new WeatherState[size];
        }
    };
    
    private WeatherState(Parcel in) {
    	cityId = in.readString();
    	city = in.readString();
    	temp = in.readString();
    	windDirect = in.readString();
    	date = in.readString();
    	windSpeed = in.readString();
    	pressure = in.readString();
    	humidity = in.readString();
    	weatherType = in.readString();
    	weatherDescription = in.readString();
    }
}
