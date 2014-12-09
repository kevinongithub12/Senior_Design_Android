package edu.oakland.racetracker;

import org.json.JSONException;
import org.json.JSONObject;

import com.mapquest.android.maps.GeoPoint;
import com.parse.ParseGeoPoint;
/**
 * 
 * @author Lukas Greib
 * Represents a location point in JSON format
 */
public class JSONPoint extends JSONObject{
	private double latitude = 0;
	private double longitude = 0;
	private double time = 0;
	
	private void update(){
		try {
			put("latitude", latitude);
			put("longitude", longitude);
			put("time", time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONPoint(){
		update();
	}
	public JSONPoint(GeoPoint gp){
		latitude = gp.getLatitude();
		longitude = gp.getLongitude();
		time = System.currentTimeMillis();
		update();
	}
	public JSONPoint(JSONObject json) throws JSONException{
		super(json.toString());
		latitude = getDouble("latitude");
		longitude = getDouble("longitude");
		time = getDouble("time");
	}
	public JSONPoint(double lat, double lon, double tim){
		latitude = lat;
		longitude = lon;
		time = tim;
		update();
	}
	public double distanceInKilometersTo(JSONPoint point){
		return new ParseGeoPoint(latitude, longitude).distanceInKilometersTo(new ParseGeoPoint(point.getLatitude(), point.getLongitude()));
	}
	public double speedBetween(JSONPoint point){
		double hour = 60*60*1000;
		double timeDelta = Math.abs(time - point.time);
		double ratio = timeDelta/hour;
		return ratio*this.distanceInKilometersTo(point)/hour;
	}
	public double getLatitude(){
		return latitude;
	}
	public void setLatitude(double lat){
		latitude = lat;
		update();
	}
	public double getLongitude(){
		return longitude;
	}
	public void setLongitude(double lon){
		longitude = lon;
		update();
	}
	public double getTime(){
		return time;
	}
	public void setTime(double tim){
		time = tim;
		update();
	}
}
