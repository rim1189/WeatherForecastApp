package com.example.weatherforecastapp;

public class Misc {
	
	public static String getWindDirection(double deg){
		if (deg < 23 || deg > 337){
			return "North";
		} else if(deg >= 23 && deg < 68){
			return "North-East";	
		} else if(deg >= 68 && deg < 113){
			return "East";	
		} else if(deg >= 113 && deg < 157){
			return "South-East";	
		} else if(deg >= 157 && deg < 203){
			return "South";	
		} else if(deg >= 203 && deg < 247){
			return "South-West";	
		} else if(deg >= 247 && deg < 293){
			return "West";	
		} else if(deg >= 293 && deg < 338){
			return "North-West";	
		} else 
			return "N/A";
	}
	
	public static int getWeatherIcon(String type){
		
			if (type.equals("01d")) {
				return R.drawable.clear_d;
		    } else if (type.equals("02d")) {
		    	return R.drawable.fclouds_d;
		    } else if (type.equals("03d")) {
		    	return R.drawable.sclouds_d;
		    } else if (type.equals("04d")) {
		    	return R.drawable.bclouds_d;
		    } else if (type.equals("09d")) {
		    	return R.drawable.srain_d;
		    } else if (type.equals("10d")) {
		    	return R.drawable.rain_d;
		    } else if (type.equals("11d")) {
		    	return R.drawable.thunderstorm_d;
		    } else if (type.equals("13d")) {
		    	return R.drawable.snow_d;
		    } else if (type.equals("50d")) {
		    	return R.drawable.mist_d;
		    } else if (type.equals("01n")) {
				return R.drawable.clear_n;
		    } else if (type.equals("02n")) {
		    	return R.drawable.fclouds_n;
		    } else if (type.equals("03n")) {
		    	return R.drawable.sclouds_n;
		    } else if (type.equals("04n")) {
		    	return R.drawable.bclouds_n;
		    } else if (type.equals("09n")) {
		    	return R.drawable.srain_n;
		    } else if (type.equals("10n")) {
		    	return R.drawable.rain_n;
		    } else if (type.equals("11n")) {
		    	return R.drawable.thunderstorm_n;
		    } else if (type.equals("13n")) {
		    	return R.drawable.snow_n;
		    } else if (type.equals("50n")) {
		    	return R.drawable.mist_n;
		    } else
		    	return R.drawable.ic_launcher;
	}

}