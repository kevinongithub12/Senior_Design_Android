package edu.oakland.racetracker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseRacer{
	private ParseUser mUser;
	public String firstName;
	public String lastName;
	public String title;
	public ParseFile avatar = new ParseFile(new byte[0]);
	public Drawable avatarDrawable;
	public int totalRaces;
	public int wins;
	public int losses;
	
	public boolean isInitiator;
	public boolean waiting;
	public ParseTrack currentTrack;
	public JSONArray recordedCoordinates;

	public ParseRacer(ParseUser user){
		mUser = user;
		firstName = mUser.getString("firstName") != null ? mUser.getString("firstName") : "";
		lastName = mUser.getString("lastName") != null ? mUser.getString("lastName") : "";
		title = mUser.getString("title") != null ? mUser.getString("title") : "";
		avatar = mUser.getParseFile("avatar") != null ? mUser.getParseFile("avatar") : new ParseFile(new byte[0]);
	    totalRaces = mUser.getInt("totalRaces");
	    wins = mUser.getInt("wins");
	    losses = mUser.getInt("losses");
	    isInitiator = mUser.getBoolean("isInitiator");
	    waiting = mUser.getBoolean("waiting");
	    currentTrack = mUser.getParseObject("currentTrack") != null ? new ParseTrack(mUser) : null;
	    recordedCoordinates = mUser.getJSONArray("recordedCoordinates") != null ? mUser.getJSONArray("recordedCoordinates") : new JSONArray();
	}
	
	public void setAvatarDrawable(Drawable drawable){
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		avatar = new ParseFile(stream.toByteArray());
	}
	
	public Drawable getAvatarDrawable(){
		avatarDrawable = null;
		try {
			avatarDrawable = Drawable.createFromStream(new ByteArrayInputStream(avatar.getData()), "");
			avatarDrawable.setBounds(0 - avatarDrawable.getIntrinsicWidth()/2, 0 - avatarDrawable.getIntrinsicHeight(), avatarDrawable.getIntrinsicWidth()/2, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return avatarDrawable;
	}
	
	public void save() throws ParseException{
		mUser.put("firstName", firstName);
		mUser.put("lastName", lastName);
		mUser.put("title", title);
		mUser.put("avatar", avatar);
		mUser.put("totalRaces", totalRaces);
		mUser.put("wins", wins);
		mUser.put("losses", losses);
		mUser.put("isInitiator", isInitiator);
		mUser.put("waiting", waiting);
		mUser.put("currentTrack", currentTrack.object);
		mUser.put("recordedCoordinates", recordedCoordinates);
		mUser.save();
	}
	public void saveInBackground(SaveCallback saveCallback){
		mUser.put("firstName", firstName);
		mUser.put("lastName", lastName);
		mUser.put("title", title);
		mUser.put("avatar", avatar);
		mUser.put("totalRaces", totalRaces);
		mUser.put("wins", wins);
		mUser.put("losses", losses);
		mUser.put("isInitiator", isInitiator);
		mUser.put("waiting", waiting);
		if (currentTrack != null) mUser.put("currentTrack", currentTrack.object);
		mUser.put("recordedCoordinates", recordedCoordinates);
		mUser.saveInBackground(saveCallback);
	}
}
