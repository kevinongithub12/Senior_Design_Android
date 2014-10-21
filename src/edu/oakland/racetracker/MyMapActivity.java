package edu.oakland.racetracker;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RectangleOverlay;
import com.parse.ParseUser;

public class MyMapActivity extends MapActivity{

	private BroadcastReceiver mLocationChangeReceiver;
	private MapView map;
	private DefaultItemizedOverlay itemOverlay;
	private LineOverlay lineOverlay;
	
	private void drawRoute(List<OverlayItem> route){
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		itemOverlay.clear();
		for(OverlayItem item : route){
			geoPoints.add(item.getPoint());
			itemOverlay.addItem(item);
		}
		if(geoPoints.isEmpty()){
			geoPoints.add(new GeoPoint(.0,.0));
		}
	    lineOverlay.setData(geoPoints);
    	map.invalidate();
	}
	
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
		
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
    	lineOverlay = new LineOverlay(paint);
    	map.getOverlays().add(lineOverlay);
		itemOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.location_marker));
		map.getOverlays().add(itemOverlay);
		
		
		
        map.getController().setZoom(5);
		map.getController().setCenter(new GeoPoint(38.0,-104.0));
		map.setBuiltInZoomControls(true);
		
		RacerListAdapter adapter = new RacerListAdapter(this, R.layout.racer_list_item);
		adapter.addAll(RaceTrackerApp.testRacers);
		ListView list = (ListView) findViewById(R.id.map_racer_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(MyMapActivity.this, ProfileActivity.class);
				intent.putExtra("profile", ((JSONRacer)arg0.getItemAtPosition(arg2)).toString());
				startActivity(intent);
			}
			
		});
		
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
				//ParseUser.getCurrentUser().
			}	
		};
		//LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mLocationChangeReceiver, new IntentFilter("location_changed"));	
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
	
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.xml.menu, menu);
	    return true;
	  }
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_load:
	    	drawRoute(MapCreatorActivity.route);
	    	Toast.makeText(this, "Loaded track!", Toast.LENGTH_LONG).show();
	      break;
	    case R.id.menu_save:
	    	Toast.makeText(this, "Saved track!", Toast.LENGTH_LONG).show();
	      break;
	    default:
	      break;
	    }
	    return true;
	  } 
}
