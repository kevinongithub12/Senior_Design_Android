package edu.oakland.racetracker;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Adapter for racer list
 * @author Lukas Greib
 *
 */
public class RacerListAdapter extends ArrayAdapter < ParseRacer > {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mRowLayout;
	public RacerListAdapter(Context context, int resource) {
		super(context, resource);
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRowLayout = resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mLayoutInflater.inflate(mRowLayout, parent, false);
		}
		
		TextView textView = (TextView) convertView.findViewById(R.id.track_list_name);
		TextView titleTextView = (TextView) convertView.findViewById(R.id.racer_list_title);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.track_list_thumb);
		TextView timeText = (TextView) convertView.findViewById(R.id.racer_list_speed);
		LinearLayout iconContainer = (LinearLayout) convertView.findViewById(R.id.track_list_thumb_container);

		ParseRacer ni = getItem(position);

		imageView.setImageDrawable(ni.getAvatarDrawable());
		titleTextView.setText(ni.title);
		textView.setText(ni.firstName + " " + ni.lastName);
		
		double speed = 0;
		if(ni.recordedCoordinates != null && ni.recordedCoordinates.length() > 1){
			try {
				JSONPoint p1 = new JSONPoint(ni.recordedCoordinates.optJSONObject(ni.recordedCoordinates.length()-2));
				JSONPoint p2 = new JSONPoint(ni.recordedCoordinates.optJSONObject(ni.recordedCoordinates.length()-1));
				speed = p2.speedBetween(p1);
			} catch (JSONException e) {e.printStackTrace();}
		}
		timeText.setText(speed + " km/h");
		iconContainer.setBackgroundColor(Color.BLACK);
		return convertView;
	}
}