package edu.oakland.racetracker;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends Activity{
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		((Button) findViewById(R.id.main_profile_button)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				RaceTrackerApp.viewingProfile = RaceTrackerApp.mRacer;
				startActivity(new Intent(MainActivity.this, ProfileActivity.class));
			}
		});
		((Button) findViewById(R.id.main_creator_test)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, MapCreatorActivity.class));
			}
		});
		((Button) findViewById(R.id.main_map_test)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    			builder.setTitle("Start Race")
    			.setCancelable(false)
    			.setPositiveButton("New", new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog, int id) {
    			    }
    			})
    			.setNegativeButton("Join", new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog, int id) {

    			    	dialog.dismiss();
 
    			    	final View popupView = getLayoutInflater().inflate(R.layout.track_select_list, null);
    			    	final ListView trackListView = (ListView) popupView.findViewById(R.id.track_select_listview);
    			    	final TrackListAdapter trackListAdapter = new TrackListAdapter(mContext, R.layout.track_select_list_item);
    			    	final LinearLayout loaderLayout = (LinearLayout) popupView.findViewById(R.id.track_select_loading);
    			    	
    			    	trackListView.setOnItemClickListener(new OnItemClickListener(){
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								RaceTrackerApp.currentTrack = (ParseTrack) arg0.getAdapter().getItem(arg2);
								startActivity(new Intent(mContext, MyMapActivity.class));
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
    						
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    	
    			    }
    			}).show();
			}
		});
		
		//REGISTRATION POPUP
		final View popupView = getLayoutInflater().inflate(R.layout.login_popup, null);
		final EditText usernameText = (EditText) popupView.findViewById(R.id.loginUsernameText);
		final EditText passwordText = (EditText) popupView.findViewById(R.id.loginPasswordText);
		final EditText emailText = (EditText) popupView.findViewById(R.id.loginEmailText);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("RaceTracker Login")
		.setCancelable(false)
		.setView(popupView);
		final AlertDialog alert = builder.create();
		alert.setButton(ProgressDialog.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {}
				});
		alert.setButton(ProgressDialog.BUTTON_POSITIVE, "Register", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {}
	            });
				alert.show();
				final Button loginButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
				loginButton.setOnClickListener(new View.OnClickListener() {
				        @Override
				        public void onClick(View v) {
				        	String username = usernameText.getText().toString();
					    	  String password = passwordText.getText().toString();
				        	ParseUser.logInInBackground(username, password, new LogInCallback(){
								@Override
								public void done(ParseUser user, ParseException arg1) {
									if(arg1 != null){
										Toast.makeText(getApplicationContext(), arg1.getMessage(), Toast.LENGTH_LONG).show();
									}
									else{
										RaceTrackerApp.mRacer = new ParseRacer(user);
										Toast.makeText(getApplicationContext(), "Parse login successful", Toast.LENGTH_LONG).show();
										alert.dismiss();
									}
								}
							  });
				        }
				});
				final Button registerButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
				registerButton.setOnClickListener(new View.OnClickListener() {
				        @Override
				        public void onClick(View v) {
				        	String username = usernameText.getText().toString();
					    	  String password = passwordText.getText().toString();
					    	  String email = emailText.getText().toString();
					    	  
					    	  if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
					    	  final ParseUser user = new ParseUser();
							  user.setUsername(username);
							  user.setPassword(password);
							  user.setEmail(email);
							  user.signUpInBackground(new SignUpCallback(){
								@Override
								public void done(ParseException arg0) {
									if(arg0 != null){
										Toast.makeText(getApplicationContext(), arg0.getMessage(), Toast.LENGTH_LONG).show();
									}
									else{
										RaceTrackerApp.mRacer = new ParseRacer(user);
									    Toast.makeText(getApplicationContext(), "Parse sign up successful", Toast.LENGTH_LONG).show();
									    alert.dismiss();
									}
								}
							  });
					    	  }
					    	  else{
					    		  Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
					    	  }
				        }
				});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
