package edu.oakland.racetracker;

import android.graphics.Bitmap;

public class Profile {
	Point lastPoint = null;
	Bitmap avatar = null;
	String userName = "Name";
	int competitions = 0;
	int wins = 0;
	int losses = 0;
	double kilometersTraveled = 0;
	double avgSpeed = 0;
	
	public void update(Point p){
		if(this.lastPoint != null){
		this.kilometersTraveled += distFrom(lastPoint.latitude, lastPoint.longitude, p.latitude, p.longitude)/1000;
		this.avgSpeed = distFrom(lastPoint.latitude, lastPoint.longitude, p.latitude, p.longitude)/1000/(60*60*100*(p.time - lastPoint.time));
		}
		lastPoint = p;
	}
	

}
