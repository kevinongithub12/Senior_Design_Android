package edu.oakland.racetracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.CircleOverlay;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.MapView.MapViewEventListener;
import com.mapquest.android.maps.Overlay.OverlayTapListener;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RectangleOverlay;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MapCreatorActivity extends MapActivity{
	private Context mContext;
	private MapView map;
	private DefaultItemizedOverlay routePointOverlay;
	private LineOverlay routeLineOverlay;
	private RectangleOverlay touchOverlay;
	private List<AnnotationView> annotations = new ArrayList<AnnotationView>();
	
	public static ParseTrack currentTrack = new ParseTrack(ParseUser.getCurrentUser());
	
	private boolean idiotFlag = false;
	
	private void drawRoute(){
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		List<Overlay> overlays = map.getOverlays();
		Iterator<Overlay> iter = overlays.iterator();
		while(iter.hasNext()){
			Overlay o = iter.next();
			if(o instanceof CircleOverlay || o instanceof LineOverlay){
				iter.remove();
			}
		}
		
		
		routePointOverlay.clear();
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
				
				OverlayItem overlayItem = new OverlayItem(gp, null, item.getDescription());
				Drawable icon = getResources().getDrawable(
						i==0 ? R.drawable.map_marker_flag_1_right_chartreuse_icon:
					    i==currentTrack.points.length()-1 ? R.drawable.map_marker_chequered_flag_right_chartreuse_icon:
					    R.drawable.map_marker_ball_chartreuse_icon
					    		);
				icon.setBounds(0 - icon.getIntrinsicWidth() / 2, 0 - icon.getIntrinsicHeight(), icon.getIntrinsicWidth() / 2, 0);
				overlayItem.setMarker(icon);
				if(!item.getDescription().isEmpty()){
				    AnnotationView a = new AnnotationView(map);
				    a.setAnimated(false);
				    a.showAnnotationView(overlayItem);
				annotations.add(a);
				}
				routePointOverlay.addItem(overlayItem);
				overlays.add(new CircleOverlay(gp, item.getRadius(), paint));
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
		routeLineOverlay = new LineOverlay(paint1);
	    routeLineOverlay.setData(geoPoints);
	    
	    //overlays.add(routePointOverlay);
		overlays.add(routeLineOverlay);
    	map.invalidate();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_map_creator);
		
		map = (MapView) findViewById(R.id.map_map);
		
		routePointOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.location_marker)){
    		@Override
		    protected boolean onTap(final int index){
    			final View popupView = getLayoutInflater().inflate(R.layout.map_creator_popup, null);
    			final EditText descriptionText = (EditText) popupView.findViewById(R.id.pointDescriptionText);
    			final CheckBox radiusCheckbox = (CheckBox) popupView.findViewById(R.id.pointRadiusCheckbox);
    			final EditText radiusText = (EditText) popupView.findViewById(R.id.pointRadiusText);
    			
    			JSONTrackPoint p = null;
		    	try {
					p = new JSONTrackPoint(currentTrack.points.getJSONObject(index));
				} catch (JSONException e) {
					e.printStackTrace();
				}
    			final JSONTrackPoint newPoint = p;
    			
    			descriptionText.setText(newPoint.getDescription());
    			radiusCheckbox.setChecked(newPoint.getImportant());
    			radiusText.setText(newPoint.getRadius()+"");
    			
    			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    			builder.setTitle("Edit Track Point")
    			.setCancelable(false)
    			.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog, int id) {
    			    	currentTrack.points.remove(index);
    			    	drawRoute();
    			    }
    			})
    			.setNegativeButton("OK", new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog, int id) {
    			    	
    			    	newPoint.setDescription(descriptionText.getText().toString());
    			    	newPoint.setImportant(radiusCheckbox.isChecked());
    			    	newPoint.setRadius(Double.parseDouble(radiusText.getText().toString()));
    			    	try {
							currentTrack.points.put(index, newPoint);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    			        dialog.cancel();
    			        drawRoute();
    			    }
    			}).setView(popupView);
    			AlertDialog alert = builder.create();
    			alert.show();
    			idiotFlag = true;
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

	    	final View popupView = getLayoutInflater().inflate(R.layout.track_select_list, null);
	    	final ListView trackListView = (ListView) popupView.findViewById(R.id.track_select_listview);
	    	final TrackListAdapter trackListAdapter = new TrackListAdapter(mContext, R.layout.track_select_list_item);
	    	final LinearLayout loaderLayout = (LinearLayout) popupView.findViewById(R.id.track_select_loading);
	    	
	    	trackListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					currentTrack = (ParseTrack) arg0.getAdapter().getItem(arg2);
					drawRoute();
				}    	
	    	});
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Select a track")
			.setCancelable(false)
			.setView(popupView)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			final AlertDialog alert = builder.create();
			alert.show();
			
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ParseTrack");
			query.findInBackground(new FindCallback<ParseObject>(){
				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {
					if(arg1 != null){
						alert.dismiss();
						Toast.makeText(getApplicationContext(), arg1.getMessage(), Toast.LENGTH_LONG).show();
					}
					else{
						trackListAdapter.clear();
						for(ParseObject o : arg0){
							trackListAdapter.add(new ParseTrack(o));
						}
						trackListView.setAdapter(trackListAdapter);
						trackListView.setVisibility(View.VISIBLE);
						loaderLayout.setVisibility(View.GONE);
					}
				}
			});
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	      break;
	    case R.id.menu_save:
	    	AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
	    	builder1.setTitle("Track Name");
	    	final EditText input = new EditText(this);
	    	builder1.setView(input);
	    	builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
	    	});
	    	builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    	  
	    	  }
	    	});
	    	final AlertDialog alert1 = builder1.create();
	    	alert1.setButton(ProgressDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					((AlertDialog) dialog).getButton(AlertDialog.BUTTON1).setEnabled(false);
					((AlertDialog) dialog).getButton(AlertDialog.BUTTON2).setEnabled(false);
				    ((AlertDialog)dialog).setView(new ProgressBar(mContext));
					String value = input.getText().toString();
			    	  ParseTrack track = new ParseTrack(ParseUser.getCurrentUser());
			    	  track.name = value;
				      track.points = currentTrack.points;
				      track.saveInBackground(new SaveCallback(){
							@Override
							public void done(ParseException arg0) {
								if(arg0 != null){
									Toast.makeText(getApplicationContext(), arg0.getMessage(), Toast.LENGTH_LONG).show();
								}
								else{
									Toast.makeText(getApplicationContext(), "Saved track!", Toast.LENGTH_LONG).show();
								}
								dialog.dismiss();
							}	
				    	});
				}
			});
	    	alert1.show();
	      break;
	    default:
	      break;
	    }
	    return true;
	  } 
}
