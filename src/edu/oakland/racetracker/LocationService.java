package edu.oakland.racetracker;

import java.util.List;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener{
    //Ignore location updates before X time or X distance change
    private static final long LOCATION_TIME_TRIGGER = 0;
	private static final float LOCATION_DISTANCE_TRIGGER = 0;
    private LocationManager mLocationManager;
	//private MockLocationProvider mock;
    private static Location mSavedLocation;
    
    //Might have to tweak this for accuracy
    private boolean isBetterLocation(Location oldLocation, Location newLocation) {
        if(oldLocation == null) {
            return true;
        }
        if(newLocation == null) {
            return false;
        }
        boolean newerAndMoreAccurate = newLocation.getTime() > oldLocation.getTime() && newLocation.getAccuracy() < oldLocation.getAccuracy();
        boolean sufficientlyNewer = newLocation.getTime() - oldLocation.getTime() > LOCATION_TIME_TRIGGER;
        boolean sufficientlyMoreAccurate = newLocation.getAccuracy() - oldLocation.getAccuracy() > LOCATION_DISTANCE_TRIGGER;
        boolean tooInaccurate = newLocation.getAccuracy() - oldLocation.getAccuracy() > LOCATION_DISTANCE_TRIGGER * 2;
        boolean tooOld = oldLocation.getTime() - newLocation.getTime()  > LOCATION_TIME_TRIGGER * 2;
        return newerAndMoreAccurate || (sufficientlyNewer && !tooInaccurate) || (sufficientlyMoreAccurate && !tooOld);     
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		Notification n = new Notification.Builder(this).setContentTitle("TITLE").setContentText("TEXT").setSmallIcon(R.drawable.ic_launcher).build();
		startForeground(0xBADA55, n);
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mSavedLocation = mLocationManager.getLastLocation();
/*
		mock = new MockLocationProvider("MOCK", this);
		 
	    //Set test location
		new android.os.Handler().postDelayed(
			    new Runnable() {
					public void run() {
			        	if(iter == 1){
			        		mock.pushLocation(34.491973,-99.799804);
			        	}
			        	if(iter == 2){
			        		mock.pushLocation(35.491973,-99.799804);
			        	}
			        	if(iter == 3){
			        		mock.pushLocation(36.491973,-99.799804);
			        	}
			        	iter++;
			        	
			        	new Handler().postDelayed(this, 10000);
			        }
			    }, 
			10000);
	*/    
		List<String> providers = mLocationManager.getAllProviders();
		for(String provider : providers){
			mLocationManager.requestLocationUpdates(provider, LOCATION_TIME_TRIGGER, LOCATION_DISTANCE_TRIGGER, this);
			Location loc = mLocationManager.getLastKnownLocation(provider);
			if(isBetterLocation(mSavedLocation, loc)){
				mSavedLocation = loc;
			}
		}
		//mLocationManager.requestLocationUpdates("MOCK", 0, 0, this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mLocationManager.removeUpdates(this);
		mLocationManager = null;
		System.gc();
	}

	@Override
	public void onLocationChanged(Location location) {
		boolean better = isBetterLocation(mSavedLocation, location);
		
			mSavedLocation = location;
			new Intent("edu.oakland.racetracker.LOCATION_CHANGED");
		LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("location_changed"));
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, provider + " disabled", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, provider + " enabled", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Toast.makeText(this, provider + " changed", Toast.LENGTH_LONG).show();
	}
	
	public static JSONPoint getLastPoint(){
		JSONPoint p = null;
		if(mSavedLocation != null){
			p = new JSONPoint(mSavedLocation.getLatitude(), mSavedLocation.getLongitude(), mSavedLocation.getTime());
		}
		return p;
	}
}
