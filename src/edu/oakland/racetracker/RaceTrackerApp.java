package edu.oakland.racetracker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mapquest.android.maps.GeoPoint;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RaceTrackerApp extends Application{

	public static ParseRacer mUser;
	//public static JSONRacer mRacer;
	//public static JSONMap mCurrentPositions; //To keep trackPoints of the player's positions
	//public static JSONMap mCurrentTrack;     //Which we can line up against this trackPoints
	
	public static List<ParseRacer> testRacers = new ArrayList<ParseRacer>();
	
	public void onCreate() {
		
		  //Parse.enableLocalDatastore(this);
		  
		  Parse.initialize(this, "fKnBZJbUYPNfV5BgcPGPAFhem5Y6Xpp5mZPkJ9fk", "nnj2aQxEGt0zmbVEArDyyNNKzDqCI144cvc05Fy8");
		  //ParseUser.enableAutomaticUser();
		  ParseUser user = new ParseUser();
		  user.setUsername("my name");
		  user.setPassword("my pass");
		  user.setEmail("email@example.com");
		  try {
			user.signUp();
		} catch (ParseException e) {
		}
		  try {
			user = ParseUser.logIn("my name", "my pass");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println();
		}
		  
		  ParseRacer morgan = new ParseRacer(ParseUser.getCurrentUser());
			morgan.setAvatarDrawable(getResources().getDrawable(R.drawable.test_morgan));
			morgan.firstName = "Morgan";
			morgan.lastName = "Freeman";
			morgan.recordedCoordinates.put(new JSONPoint(new GeoPoint(44.2932, -105.249023)));
			testRacers.add(morgan);

		  mUser = new ParseRacer(user);
		  mUser.setAvatarDrawable(getResources().getDrawable(R.drawable.test_sanic));
		  
		  mUser.recordedCoordinates.put(new JSONPoint());
		  mUser.recordedCoordinates.put(new JSONPoint());
		  mUser.recordedCoordinates.put(new JSONPoint());
		  
		  mUser.saveInBackground(new SaveCallback(){
			@Override
			public void done(ParseException error) {
				if(error != null){
					System.out.println(error.getMessage());
				}
			}
		  });
	}
}
