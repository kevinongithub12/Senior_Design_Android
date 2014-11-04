package edu.oakland.racetracker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseRacer{
	private ParseUser mUser;
	
	public String firstName = "";
	public String lastName = "";
	public String title = "";
	public ParseFile avatar = new ParseFile(new byte[0]);
	public int totalRaces = 0;
	public int wins = 0;
	public int losses = 0;
	
	public boolean waiting = false;
	public ParseObject currentTrack = new ParseObject("ParseTrack");
	public JSONArray recordedCoordinates = new JSONArray();

	public ParseRacer(ParseUser user){
		mUser = user;
		try {
		    avatar = mUser.getParseFile("avatar");
		    if(avatar == null){
		    	throw new Exception();
		    }
		} catch(Exception e){
			avatar = new ParseFile(new byte[0]);
			try {
				avatar.save();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void setAvatarDrawable(Drawable drawable){
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		avatar = new ParseFile(stream.toByteArray());
	}
	
	public Drawable getAvatarDrawable(){
		Drawable icon = null;
		try {
			icon = Drawable.createFromStream(new ByteArrayInputStream(avatar.getData()), "");
			icon.setBounds(0 - icon.getIntrinsicWidth()/2, 0 - icon.getIntrinsicHeight(), icon.getIntrinsicWidth()/2, 0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return icon;
	}
	
	public void save() throws ParseException{
		mUser.put("firstName", firstName);
		mUser.put("lastName", lastName);
		mUser.put("title", title);
		mUser.put("avatar", avatar);
		mUser.put("totalRaces", totalRaces);
		mUser.put("wins", wins);
		mUser.put("losses", losses);
		mUser.put("waiting", waiting);
		mUser.put("currentTrack", currentTrack);
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
		mUser.put("waiting", waiting);
		mUser.put("currentTrack", currentTrack);
		mUser.put("recordedCoordinates", recordedCoordinates);
		mUser.saveInBackground(saveCallback);
	}
}
