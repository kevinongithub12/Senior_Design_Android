package edu.oakland.racetracker;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.CircleOverlay;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MapView.MapViewEventListener;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.OverlayItem;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
/**
 * 
 * @author Lukas Greib
 * The racing map activity
 */
public class MyMapActivity extends MapActivity{
    private Context mContext;
	private BroadcastReceiver mLocationChangeReceiver;
	private MapView map;
	private List<AnnotationView> annotations = new ArrayList<AnnotationView>();
	private ParseTrack currentTrack;
	private List<ParseRacer> racers = new ArrayList<ParseRacer>();
	private PopDialog popup;
	private DefaultItemizedOverlay routePointOverlay;
	private DefaultItemizedOverlay racerPointOverlay;
	private ListView list;
	private RacerListAdapter adapter;
	
	private void findParticipants(){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.include("currentTrack");
		//query.whereEqualTo("currentTrack", currentTrack.object.getObjectId());
		//query.whereMatches("currentTrack", currentTrack.object.getObjectId());
		query.findInBackground(new FindCallback<ParseUser>(){
			@Override
			public void done(List<ParseUser> arg0, ParseException arg1) {
				if(arg1 != null){
					Toast.makeText(mContext, arg1.getMessage(), Toast.LENGTH_LONG).show();
				}
				else{
				racers.clear();
				for(ParseUser pu : arg0){	
					if(pu.getParseObject("currentTrack") != null && currentTrack.name.equals(pu.getParseObject("currentTrack").getString("name"))){
						ParseRacer r = new ParseRacer(pu);
						if(r.isInitiator && !r.waiting){
							popup.dismiss();
						}
						racers.add(r);
					}
					
				}
				adapter.clear();
				adapter.addAll(racers);
				drawRoute();
				checkWin();
				}
			}
		});
	}
	
	private void checkWin(){
		for(ParseRacer r : racers){
			for(int i = 0; i < r.recordedCoordinates.length(); i++){
				JSONTrackPoint end;
				try {
					end = new JSONTrackPoint(currentTrack.points.getJSONObject(currentTrack.points.length()-1));
					JSONPoint p = new JSONPoint(r.recordedCoordinates.getJSONObject(i));
					if(end != null && p.distanceInKilometersTo(end)*1000 < end.getRadius()){
						System.out.println(p.distanceInKilometersTo(end));
						System.out.println(end.getRadius());
						endRace(r.firstName.equals(RaceTrackerApp.mRacer.firstName));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void endRace(final boolean win){
		stopService(new Intent(mContext, LocationService.class));
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Game Over")
		.setMessage(win?"You win!":"You lose! Good day, sir!")
		.setCancelable(false)
		.setNegativeButton(win?"Yay":"Awww...", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(final DialogInterface dialog, int which) {
				RaceTrackerApp.mRacer.totalRaces++;
				RaceTrackerApp.mRacer.wins += win ? 1:0;
				RaceTrackerApp.mRacer.losses += win ? 0:1;
				RaceTrackerApp.mRacer.saveInBackground(new SaveCallback(){
					@Override
					public void done(ParseException arg0) {
						dialog.dismiss();
						finish();
					}
					
				});
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	private void drawRoute(){
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		List<Overlay> overlays = map.getOverlays();
		overlays.clear();
		
		for(AnnotationView a : annotations){
			a.hide();
		}
		annotations.clear();

		Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
		for(int i = 0; i < currentTrack.points.length(); i++){
			try {
				JSONTrackPoint item = new JSONTrackPoint(currentTrack.points.getJSONObject(i));
				GeoPoint gp = new GeoPoint(item.getLatitude(), item.getLongitude());
				geoPoints.add(gp);
				
				OverlayItem overlayItem = new OverlayItem(gp, item.getDescription(), "");
				Drawable icon = getResources().getDrawable(
						i==0 ? R.drawable.map_marker_flag_1_right_chartreuse_icon:
					    i==currentTrack.points.length()-1 ? R.drawable.map_marker_chequered_flag_right_chartreuse_icon:
					    R.drawable.map_marker_ball_chartreuse_icon
					    		);
				icon.setBounds(0 - icon.getIntrinsicWidth() / 2, 0 - icon.getIntrinsicHeight(), icon.getIntrinsicWidth() / 2, 0);
				overlayItem.setMarker(icon);
				routePointOverlay.addItem(overlayItem);
				overlays.add(new CircleOverlay(gp, item.getRadius(), paint));
				if(!item.getDescription().isEmpty()){
				    AnnotationView a = new AnnotationView(map);
				    a.setAnimated(false);
				    a.showAnnotationView(overlayItem);
				annotations.add(a);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if(geoPoints.isEmpty()){
			geoPoints.add(new GeoPoint(.0,.0));
		}

		Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(Color.BLUE);
        paint1.setAlpha(100);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeJoin(Paint.Join.ROUND);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setStrokeWidth(5);
		LineOverlay routeLineOverlay = new LineOverlay(paint1);
	    routeLineOverlay.setData(geoPoints);
	    for(ParseRacer racer : racers){
	    	JSONPoint last;
			try {
				System.out.println(racer.firstName);
				last = new JSONPoint(racer.recordedCoordinates.getJSONObject(racer.recordedCoordinates.length()-1));
				OverlayItem racerItem = new OverlayItem(new GeoPoint(last.getLatitude(), last.getLongitude()), "", racer.firstName+" "+racer.lastName);
		    	racerItem.setMarker(racer.getAvatarDrawable());
		    	racerPointOverlay.addItem(racerItem);
		    	if(!racer.firstName.isEmpty()){
				    AnnotationView a = new AnnotationView(map);
				    a.setAnimated(false);
				    a.showAnnotationView(racerItem);
				annotations.add(a);
				}    	
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    overlays.add(routeLineOverlay);
		overlays.add(routePointOverlay);
		overlays.add(racerPointOverlay);
		map.invalidate();
	}
	
	private void autoZoom(){
		if(currentTrack != null && currentTrack.points != null){
			List<GeoPoint> points = new ArrayList<GeoPoint>();
			for(int i = 0; i < currentTrack.points.length(); i++){
				try {
					
					JSONTrackPoint item = new JSONTrackPoint(currentTrack.points.getJSONObject(i));
					points.add(new GeoPoint(item.getLatitude(), item.getLongitude()));
				}catch(JSONException e){}
			}
			map.getController().zoomToSpan(BoundingBox.calculateBoundingBoxGeoPoint(points));
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = this;
		currentTrack = RaceTrackerApp.mRacer.currentTrack;
		findParticipants();
		
		startService(new Intent(mContext, LocationService.class));
		setContentView(R.layout.activity_mymap);
		
		TabHost tabHost = (TabHost) findViewById(R.id.map_tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("Map").setContent(R.id.map_map_container).setIndicator("Map", null));
		tabHost.addTab(tabHost.newTabSpec("Racers").setContent(R.id.map_racer_list_container).setIndicator("Racers", null));
		
		popup = new PopDialog(mContext);
		popup.getAlert().setView(new ProgressBar(mContext));
		popup.getAlert().setCancelable(false);
		popup.getAlert().setMessage(RaceTrackerApp.mRacer.isInitiator?"Press Ok when ready":"Please wait...");
		if(RaceTrackerApp.mRacer.isInitiator){
		popup.getAlert().setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RaceTrackerApp.mRacer.waiting = false;
			}
		});
		}
		popup.show(getFragmentManager(), "");
			
		map = (MapView) findViewById(R.id.map_map);
		routePointOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.location_marker));
		racerPointOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.location_marker));		
        map.getController().setZoom(5);
		map.getController().setCenter(new GeoPoint(38.0,-104.0));
		map.setBuiltInZoomControls(true);
		map.addMapViewEventListener(new MapViewEventListener(){
			@Override
			public void longTouch(MapView arg0) {}
			@Override
			public void mapLoaded(MapView arg0) {}
			@Override
			public void move(MapView arg0) {}
			@Override
			public void moveEnd(MapView arg0) {
				//drawRoute();
			}
			@Override
			public void moveStart(MapView arg0) {}
			@Override
			public void touch(MapView arg0) {}
			@Override
			public void zoomEnd(MapView arg0) {
				//drawRoute();
			}
			@Override
			public void zoomStart(MapView arg0) {}
		});
		autoZoom();
		drawRoute();
		adapter = new RacerListAdapter(this, R.layout.racer_list_item);
		list = (ListView) findViewById(R.id.map_racer_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				RaceTrackerApp.viewingProfile = (ParseRacer)arg0.getItemAtPosition(arg2);
				startActivity(new Intent(mContext, ProfileActivity.class));
			}
		});
		
		mLocationChangeReceiver = new BroadcastReceiver(){
			private JSONPoint p;
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				p = LocationService.getLastPoint();
				Toast.makeText(mContext, "New Location", Toast.LENGTH_LONG).show();
				if(currentTrack != null){
				RaceTrackerApp.mRacer.recordedCoordinates.put(p);
				RaceTrackerApp.mRacer.saveInBackground(new SaveCallback(){
					@Override
					public void done(ParseException arg0) {
						if(arg0 != null){
							Toast.makeText(mContext, arg0.getMessage(), Toast.LENGTH_LONG).show();
						}
						else{
							findParticipants();
						}
					}				
				});
				drawRoute();
				}
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
	    /*switch (item.getItemId()) {
	    case R.id.menu_load:
	    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseTrack");
	    	query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
	    	try {
				List<ParseObject> savedTracks = query.find();
				currentTrack = new ParseTrack(savedTracks.get(0));
				drawRoute();
				Toast.makeText(this, "Loaded trackPoints!", Toast.LENGTH_LONG).show();
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    	Toast.makeText(this, "Loaded trackPoints!", Toast.LENGTH_LONG).show();
	      break;
	    case R.id.menu_save:
	    	Toast.makeText(this, "Saved trackPoints!", Toast.LENGTH_LONG).show();
	      break;
	    default:
	      break;
	    }*/
	    return true;
	  } 
}
