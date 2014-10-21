package edu.oakland.racetracker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class RaceTrackerApp extends Application{

	public static ParseUser mUser;
	public static JSONRacer mRacer;
	public static JSONMap mCurrentPositions; //To keep track of the player's positions
	public static JSONMap mCurrentTrack;     //Which we can line up against this track
	
	
	public static List<JSONRacer> testRacers = new ArrayList<JSONRacer>();
	
	public void onCreate() {
		
		try {
			testRacers.add(new JSONRacer(new JSONObject(JSONRacer.MORGAN)));
			testRacers.add(new JSONRacer(new JSONObject(JSONRacer.TINA)));
			testRacers.add(new JSONRacer(new JSONObject(JSONRacer.SANIC)));
			testRacers.add(new JSONRacer(new JSONObject(JSONRacer.SQUIDWARD)));
		} catch (JSONException e2) {}
		
		
		  Parse.enableLocalDatastore(this);
		  Parse.initialize(this, "fKnBZJbUYPNfV5BgcPGPAFhem5Y6Xpp5mZPkJ9fk", "nnj2aQxEGt0zmbVEArDyyNNKzDqCI144cvc05Fy8");
		  ParseUser.enableAutomaticUser();
		  mUser = ParseUser.getCurrentUser();
		  
		  ArrayList<JSONMap> maps = new ArrayList<JSONMap>();
		  maps.add(new JSONMap());
		  ParseObject p = new ParseObject("Map");
		  p.addAllUnique("JSONMap", maps);
		  p.saveInBackground();
		  mUser.saveInBackground();
		  
		  try {
				mRacer = new JSONRacer(mUser.getJSONObject("racerProfile"));
			} catch (Exception e) {
				try {
					mUser.put("racerProfile", new JSONRacer());
					mUser.saveInBackground();
				} catch (JSONException e1) {}
			}
		  mCurrentPositions = new JSONMap();
		  mCurrentTrack = new JSONMap();
		  try {
			mRacer = new JSONRacer();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
