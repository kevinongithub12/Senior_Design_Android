package edu.oakland.racetracker;

import org.json.JSONArray;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseTrack{
	public ParseObject object = new ParseObject("ParseTrack");
	public ParseUser createdBy;
	public JSONArray points = new JSONArray();
	public String name = "";
	public boolean shared = false;
	public ParseTrack(ParseUser user){
		createdBy = user;
	}
	public ParseTrack(ParseObject object){
		createdBy = (ParseUser) object.get("createdBy");
		points = object.getJSONArray("points");
		name = object.getString("name");
		shared = object.getBoolean("shared");
	}
	public void save(){
		object.put("createdBy", createdBy);
		object.put("points", points);
		object.put("name", name);
		object.put("shared", shared);
		try {
			object.save();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public void saveInBackground(SaveCallback callback){
		object.put("createdBy", createdBy);
		object.put("points", points);
		object.put("name", name);
		object.put("shared", shared);
		object.saveInBackground(callback);
	}
}
