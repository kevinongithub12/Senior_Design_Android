package edu.oakland.racetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity{
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
		setContentView(R.layout.activity_mymap);
		
		TabHost tabHost = (TabHost) findViewById(R.id.map_tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("Map").setContent(R.id.map_map_container).setIndicator("Map", null));
		tabHost.addTab(tabHost.newTabSpec("Racers").setContent(R.id.map_racer_list_container).setIndicator("Racers", null));
		
		map = (MapView) findViewById(R.id.map_map);
        map.getController().setZoom(5);
		map.getController().setCenter(new GeoPoint(38.0,-104.0));
		map.setBuiltInZoomControls(true);
		
		mLocationChangeReceiver = new BroadcastReceiver(){
			private DefaultItemizedOverlay overlay;
			private OverlayItem item2;
			private Drawable icon;
			private Point p;

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				p = LocationService.getLastPoint();
				icon = getResources().getDrawable(R.drawable.location_marker);
				overlay = new DefaultItemizedOverlay(icon);
		    	item2 = new OverlayItem(new GeoPoint(p.getLatitude(), p.getLongitude()), "Some Location", "???");
		    	overlay.addItem(item2);
		    	map.getOverlays().add(overlay);
		    	map.invalidate();
				map.getController().animateTo(new GeoPoint(p.getLatitude(), p.getLongitude()));
				map.getController().setZoom(5);
				RaceTrackerApp.mProfile.update(p);
			}	
		};
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mLocationChangeReceiver, new IntentFilter("location_changed"));

	
		
	
	
	
	
	
	
	
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mLocationChangeReceiver != null){
			LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mLocationChangeReceiver);
		}
		stopService(new Intent(this, LocationService.class));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
