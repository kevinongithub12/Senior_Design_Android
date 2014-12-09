package edu.oakland.racetracker;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
 * @author Lukas Greib
 * JSON Point used to represent a track
 */
public class JSONTrackPoint extends JSONPoint{
	private double radius = 0;
	private boolean important = false;
	private String description = "";
	
	private void update(){
		try {
			put("radius", radius);
			put("important", important);
			put("description", description);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONTrackPoint(){
		update();
	}
	public JSONTrackPoint(JSONObject json) throws JSONException{
		super(json);
		radius = getDouble("radius");
		important = getBoolean("important");
		description = getString("description");
	}
	public JSONTrackPoint(double lat, double lon, double tim, double rad, boolean imp, String desc){
		super(lat, lon, tim);
		radius = rad;
		important = imp;
		description = desc;
		update();
	}
	
	public double getRadius(){
		return radius;
	}
	public void setRadius(double rad){
		radius = rad;
		update();
	}
	public boolean getImportant(){
		return important;
	}
	public void setImportant(boolean imp){
		important = imp;
		update();
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String desc){
		description = desc;
		update();
	}
}
