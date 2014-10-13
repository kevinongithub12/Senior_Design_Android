package edu.oakland.racetracker;

import org.json.JSONArray;
import org.json.JSONException;

import com.parse.ParseGeoPoint;

public class Point extends ParseGeoPoint{
	private double mTime;
	public Point(JSONArray json) throws JSONException{
		this(json.getDouble(0), json.getDouble(1), json.getDouble(2));
	}
	public Point(double latitude, double longitude, double time) {
		super(latitude, longitude);
		mTime = time;
	}
	public double getTime(){
		return mTime;
	}
	public double timeBetween(Point p){
		return mTime - p.getTime();
	}
	public double speedBetween(Point p){
		double hour = (60*60*1000);
		double timeDelta = mTime - p.getTime();
		double ratio = timeDelta/hour;
		return ratio*distanceInKilometersTo(p)/hour;
	}
	public JSONArray toJSONArray(){
		JSONArray json = new JSONArray();
		try {
			json.put(getLatitude());
			json.put(getLongitude());
			json.put(getTime());
		} catch (JSONException e) {}
		return json;
	}
}
