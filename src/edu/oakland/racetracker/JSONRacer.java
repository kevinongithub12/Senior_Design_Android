package edu.oakland.racetracker;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONRacer extends JSONObject{
	public static final String EMPTY = "{firstName:'',lastName:'',title:'',avatar:-1,totalRaces:0,wins:0,losses:0,totalDistance:0,currentSpeed:0}";
	public static final String TINA = "{firstName:'Terra',lastName:'Branford',title:'Magitek Elite',avatar:"+R.drawable.test_tina+",totalRaces:100,wins:53,losses:47,totalDistance:10000,currentSpeed:10}";
	public static final String MORGAN = "{firstName:'Morgan',lastName:'Freeman',title:'God',avatar:"+R.drawable.test_morgan+",totalRaces:1000,wins:1000,losses:0,totalDistance:100000,currentSpeed:999}";
	public static final String SQUIDWARD = "{firstName:'Squidward',lastName:'Tentacles',title:'Handsome Squidward',avatar:"+R.drawable.test_squidward+",totalRaces:9,wins:4,losses:5,totalDistance:47,currentSpeed:4}";
	public static final String SANIC = "{firstName:'Sanic',lastName:'Hegehog',title:'UR 2 SLO',avatar:"+R.drawable.test_sanic+",totalRaces:255,wins:200,losses:55,totalDistance:12345,currentSpeed:40}";
	public JSONRacer() throws JSONException{
		this(new JSONObject(EMPTY));
	}
	
	public JSONRacer(JSONObject json) throws JSONException{
	    super(json.toString());
	}
	
	//Getters
	public int getAvatar(){
		int x = -1;
		try {x = this.getInt("avatar");} catch (JSONException e) {}
		return x; 
	}
	public String getFirstName(){
		String x = "";
		try {x = this.getString("firstName");} catch (JSONException e) {}
		return x; 
	}
	public String getLastName(){
		String x = "";
		try {x = this.getString("lastName");} catch (JSONException e) {}
		return x; 
	}
	public String getTitle(){
		String x = "";
		try {x = this.getString("title");} catch (JSONException e) {}
		return x; 
	}
	public int getTotalRaces(){
		int x = 0;
		try {x = this.getInt("totalRaces");} catch (JSONException e) {}
		return x; 
	}
	public int getWins(){
		int x = 0;
		try {x = this.getInt("wins");} catch (JSONException e) {}
		return x; 
	}
	public int getLosses(){
		int x = 0;
		try {x = this.getInt("losses");} catch (JSONException e) {}
		return x; 
	}
	public int getTotalDistance(){
		int x = 0;
		try {x = this.getInt("totalDistance");} catch (JSONException e) {}
		return x; 
	}
	public int getCurrentSpeed(){
		int x = 0;
		try {x = this.getInt("currentSpeed");} catch (JSONException e) {}
		return x; 
	}
	
	
	
	
	//Setters
	public void setAvatar(int x){
		try {this.put("avatar", x);} catch (JSONException e) {}
	}
	public void setFirstName(String x){
		try {this.put("firstName", x);} catch (JSONException e) {}
	}
	public void setLastName(String x){
		try {this.put("lastName", x);} catch (JSONException e) {}
	}
	public void setTitle(String x){
		try {this.put("title", x);} catch (JSONException e) {}
	}
	public void setTotalRaces(int x){
		try {this.put("totalRaces", x);} catch (JSONException e) {}
	}
	public void setWins(int x){
		try {this.put("wins", x);} catch (JSONException e) {}
	}
	public void setLosses(int x){
		try {this.put("losses", x);} catch (JSONException e) {}
	}
	public void setTotalDistance(int x){
		try {this.put("totalDistance", x);} catch (JSONException e) {}	 
	}
	public void setCurrentSpeed(int x){
		try {this.put("currentSpeed", x);} catch (JSONException e) {}
	}
	/*
	public void updatePosition(Point p){
		if(this.lastPoint != null){
		this.kilometersTraveled += distFrom(lastPoint.latitude, lastPoint.longitude, p.latitude, p.longitude)/1000;
		this.avgSpeed = distFrom(lastPoint.latitude, lastPoint.longitude, p.latitude, p.longitude)/1000/(60*60*100*(p.time - lastPoint.time));
		}
		lastPoint = p;
	}*/
}
