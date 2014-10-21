package edu.oakland.racetracker;

import java.text.SimpleDateFormat;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RacerListAdapter extends ArrayAdapter < JSONRacer > {
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
		
		TextView textView = (TextView) convertView.findViewById(R.id.racer_list_name);
		TextView titleTextView = (TextView) convertView.findViewById(R.id.racer_list_title);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.racer_list_avatar);
		TextView timeText = (TextView) convertView.findViewById(R.id.racer_list_speed);
		LinearLayout iconContainer = (LinearLayout) convertView.findViewById(R.id.racer_list_avatar_container);

		JSONRacer ni = getItem(position);

		imageView.setImageDrawable(mContext.getResources().getDrawable(ni.getAvatar()));
		titleTextView.setText(ni.getTitle());
		textView.setText(ni.getFirstName() + " " + ni.getLastName());
		timeText.setText(ni.getCurrentSpeed() + "km/h");
		iconContainer.setBackgroundColor(Color.BLACK);
		return convertView;
	}
}