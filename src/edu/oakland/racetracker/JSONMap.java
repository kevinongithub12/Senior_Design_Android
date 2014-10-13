package edu.oakland.racetracker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMap extends JSONObject{//Represents tracks and racer positions
	private ArrayList<Point> mPoints = new ArrayList<Point>();
	
	public JSONMap(){
		super();
	}
	
	public JSONMap(String str) throws JSONException{
		super(str);
	}
	
	public void addPoint(Point p){
		mPoints.add(p);
		compilePoints();
	}
	public void removePoint(int index){
		if(index > -1 && index < mPoints.size()){
			mPoints.remove(index);
			compilePoints();
		}
	}
	public ArrayList<Point> getPoints(){
		return mPoints;
	}
	private void compilePoints(){
		JSONArray points = new JSONArray();
		try {
		int l = mPoints.size();
		for(int i = 0; i < l; i++){
			points.put(i,mPoints.get(i).toJSONArray());
		}
		
			this.put("points", points);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
