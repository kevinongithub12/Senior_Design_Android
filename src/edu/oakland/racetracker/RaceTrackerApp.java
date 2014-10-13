package edu.oakland.racetracker;

import org.json.JSONException;

import android.app.Application;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class RaceTrackerApp extends Application{
	
	public static Profile mProfile;
	
	public static ParseUser mUser;
	public static JSONRacer mRacer;
	
	public static void saveRacerProfile(){
		if(mUser != null && mRacer != null){
		}
	}
	
	public void onCreate() {
		  Parse.initialize(this, "fKnBZJbUYPNfV5BgcPGPAFhem5Y6Xpp5mZPkJ9fk", "nnj2aQxEGt0zmbVEArDyyNNKzDqCI144cvc05Fy8");
		  ParseObject testObject = new ParseObject("TestObject");
		  testObject.put("foo", "bar");
		  //testObject.saveInBackground();
		ParseUser.logInInBackground("", "", new LogInCallback(){

			@Override
			public void done(ParseUser arg0, ParseException e) {
				if(e == null){
					mUser = arg0;
					try {
						mRacer = new JSONRacer(mUser.getJSONObject("racerProfile").toString());
					} catch (JSONException e1) {e1.printStackTrace();}
				}
				
			}
			
		});
		  
		  mProfile = new Profile();
		}
}
