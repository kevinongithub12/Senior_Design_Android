package edu.oakland.racetracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.parse.Parse;
/**
 * Main class to store some global variables
 * @author Lukas Greib
 *
 */
public class RaceTrackerApp extends Application{

	public static ParseRacer mRacer;
	public static ParseRacer viewingProfile;
	public static List<ParseRacer> testRacers = new ArrayList<ParseRacer>();
	
	public void onCreate() {
		  //Parse.enableLocalDatastore(this);
		  Parse.initialize(this, "fKnBZJbUYPNfV5BgcPGPAFhem5Y6Xpp5mZPkJ9fk", "nnj2aQxEGt0zmbVEArDyyNNKzDqCI144cvc05Fy8");
		  //ParseUser.enableAutomaticUser();
		  //ParseUser user = new ParseUser();
		  //ParseUser user = ParseUser.getCurrentUser();
		  /*
		  ParseRacer morgan = new ParseRacer(new ParseUser());
			morgan.setAvatarDrawable(getResources().getDrawable(R.drawable.test_morgan));
			morgan.firstName = "Morgan";
			morgan.lastName = "Freeman";
			morgan.recordedCoordinates.put(new JSONPoint(new GeoPoint(44.2932, -105.249023)));
			testRacers.add(morgan);*/
	}
}
