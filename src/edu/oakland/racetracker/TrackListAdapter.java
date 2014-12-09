package edu.oakland.racetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * Adapter for track list
 * @author Lukas Greib
 *
 */
public class TrackListAdapter extends ArrayAdapter < ParseTrack > {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mRowLayout;
	public TrackListAdapter(Context context, int resource) {
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

		ParseTrack ni = getItem(position);
		
		if(textView == null){
			System.out.println("CRAP");
		}
		else{
		textView.setText(ni.name);
		if(ni.points != null){
			System.out.println(ni.points.toString());
			
		}
		}
		return convertView;
	}
}