package edu.oakland.racetracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MapView.MapViewEventListener;
import com.mapquest.android.maps.Overlay.OverlayTapListener;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RectangleOverlay;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MapCreatorActivity extends MapActivity{
	private MapView map;
	private DefaultItemizedOverlay routePointOverlay;
	private LineOverlay routeLineOverlay;
	private RectangleOverlay touchOverlay;
	
	public static ParseTrack currentTrack = new ParseTrack(ParseUser.getCurrentUser());
	
	private boolean idiotFlag = false;
	
	private void drawRoute(){
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		routePointOverlay.clear();
		for(int i = 0; i < currentTrack.points.length(); i++){
			try {
				JSONTrackPoint item = new JSONTrackPoint(currentTrack.points.getJSONObject(i));
				GeoPoint gp = new GeoPoint(item.getLatitude(), item.getLongitude());
				geoPoints.add(gp);
				routePointOverlay.addItem(new OverlayItem(gp, item.getDescription(), ""));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(geoPoints.isEmpty()){
			geoPoints.add(new GeoPoint(.0,.0));
		}
	    routeLineOverlay.setData(geoPoints);
    	map.invalidate();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_creator);
		
		map = (MapView) findViewById(R.id.map_map);
		
		routePointOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.location_marker)){
    		@Override
		    protected boolean onTap(int index){
    			idiotFlag = true;
    			currentTrack.points.remove(index);
    			drawRoute();
		    	return true;
		    }
    	};
    	
    	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
    	routeLineOverlay = new LineOverlay(paint);

    	Paint paint1 = new Paint();
        paint1.setColor(Color.YELLOW);
        paint1.setStrokeWidth(10);
        paint1.setAlpha(50);
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.FILL);
        touchOverlay = new RectangleOverlay(map.getBoundingBox(null), paint1);
        touchOverlay.setTapListener(new OverlayTapListener(){
			@Override
			public void onTap(GeoPoint arg0, MapView arg1) {
				if(!idiotFlag){
					JSONPoint newPoint = new JSONTrackPoint(arg0.getLatitude(), arg0.getLongitude(), System.currentTimeMillis(), 50, true, "This is a point!");
					currentTrack.points.put(newPoint);
				    drawRoute();
				}
				else{
					idiotFlag = false;
				}
			}
		});
		
        map.getController().setZoom(5);
		map.getController().setCenter(new GeoPoint(38.0,-104.0));
		map.setBuiltInZoomControls(true);

        routeLineOverlay.setKey("Line #1");
        
        map.getOverlays().add(routeLineOverlay);
        map.getOverlays().add(touchOverlay);
    	map.getOverlays().add(routePointOverlay);
		
		
		map.invalidate();
		map.addMapViewEventListener(new MapViewEventListener(){
			@Override
			public void longTouch(MapView arg0) {}
			@Override
			public void mapLoaded(MapView arg0) {
				touchOverlay.setBoundingBox(map.getBoundingBox(null));
			}
			@Override
			public void move(MapView arg0) {}
			@Override
			public void moveEnd(MapView arg0) {
				touchOverlay.setBoundingBox(map.getBoundingBox(null));
			}
			@Override
			public void moveStart(MapView arg0) {}
			@Override
			public void touch(MapView arg0) {}
			@Override
			public void zoomEnd(MapView arg0) {
				touchOverlay.setBoundingBox(map.getBoundingBox(null));
			}
			@Override
			public void zoomStart(MapView arg0) {}
		});
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
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
	    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseTrack");
	    	query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
	    	try {
				List<ParseObject> savedTracks = query.find();
				for(ParseObject po : savedTracks){
					Set<String> keys = po.keySet();
					for(String key : keys){
						System.out.println(key);
					}
				}
				
				currentTrack = new ParseTrack(savedTracks.get(0));
				drawRoute();
				Toast.makeText(this, "Loaded trackPoints!", Toast.LENGTH_LONG).show();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      break;
	    case R.id.menu_save:
	    	ParseTrack track = new ParseTrack(ParseUser.getCurrentUser());
	    	track.name = "TEST";
	    	track.points = currentTrack.points;
	    	track.save();
	    	Toast.makeText(this, "Saved trackPoints!", Toast.LENGTH_LONG).show();
	      break;
	    default:
	      break;
	    }
	    return true;
	  } 
}
