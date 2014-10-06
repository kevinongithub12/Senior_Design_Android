package edu.oakland.racetracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;

public class MainActivity extends MapActivity{
	private NetworkHelper mNetworkHelper;
	private TextView mTextOutput;
	private EditText mURL, mUserID, mFirstName, mLastName, mTimeStamp;
	private Button mGetButton;
	
	private String url, userId, firstName, lastName, timeStamp;
	private BroadcastReceiver mLocationChangeReceiver;

	private MapView map;
	/*urlParameters = "serial=" + URLEncoder.encode(android.os.Build.SERIAL, "UTF-8") +
	"&UserID=" + URLEncoder.encode(userId, "UTF-8") +
	"&FirstName=" + URLEncoder.encode(firstName, "UTF-8") +
	"&LastName=" + URLEncoder.encode(lastName, "UTF-8") +
	"&TimeStamp=" + URLEncoder.encode(timeStamp, "UTF-8") +
	"&model=" + URLEncoder.encode(android.os.Build.MODEL, "UTF-8");*/
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		startService(new Intent(this, LocationService.class));
		setContentView(R.layout.activity_main);
		map = (MapView) findViewById(R.id.map);
        map.getController().setZoom(5);
		map.getController().setCenter(new GeoPoint(38.0,-104.0));
		map.setBuiltInZoomControls(true);
		
		mLocationChangeReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				Point p = LocationService.getLastPoint();
				map.getController().setCenter(new GeoPoint(p.latitude, p.longitude));
				map.getController().setZoom(20);
			}	
		};
		LocalBroadcastManager.getInstance(this).registerReceiver(mLocationChangeReceiver, new IntentFilter("location_changed"));
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mLocationChangeReceiver != null){
		    unregisterReceiver(mLocationChangeReceiver);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
