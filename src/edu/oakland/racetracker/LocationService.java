package edu.oakland.racetracker;

import java.util.List;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener{
    //Ignore location updates before X time or X distance change
    private static final long LOCATION_TIME_TRIGGER = 1000*30;
	private static final float LOCATION_DISTANCE_TRIGGER = 10;
	 private int iter;
    private LocationManager mLocationManager;
	private MockLocationProvider mock;
    private static Location mSavedLocation;
    
    //Might have to tweak this for accuracy
    private boolean isBetterLocation(Location oldLocation, Location newLocation) {
        if(oldLocation == null) {
            return true;
        }
        boolean isNewer = newLocation.getTime() > oldLocation.getTime();
        boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();       
        if(isMoreAccurate && isNewer) {         
            return true;     
        } else if(isMoreAccurate && !isNewer) {               
            long timeDifference = newLocation.getTime() - oldLocation.getTime(); 
            if(timeDifference > LOCATION_TIME_TRIGGER) {
                return true;
            }
        }
        return false;
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
		List<String> providers = mLocationManager.getAllProviders();
		
		
		mock = new MockLocationProvider("MOCK", this);
		 
	    //Set test location
		new android.os.Handler().postDelayed(
			    new Runnable() {
			       

					public void run() {
			        	if(iter ==1){
			        		mock.pushLocation(40.7127,-74.0059);
			        	}
			        	if(iter == 2){
			        		mock.pushLocation(42.3314,-83.0458);
			        	}
			        	if(iter == 3){
			        		mock.pushLocation(41.8369,-87.6847);
			        	}
			        	iter++;
			        	
			        	new Handler().postDelayed(this, 10000);
			        }
			    }, 
			10000);
	    
		
		//for(String provider : providers){
			mLocationManager.requestLocationUpdates("MOCK", 0, 0, this);
		//}
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
		
		Toast.makeText(this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
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
	
	public static Point getLastPoint(){
		Point p = null;
		if(mSavedLocation != null){
			p = new Point(mSavedLocation.getLatitude(), mSavedLocation.getLongitude(), mSavedLocation.getTime());
		}
		return p;
	}
}
