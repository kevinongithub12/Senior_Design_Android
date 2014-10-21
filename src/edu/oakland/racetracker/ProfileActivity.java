package edu.oakland.racetracker;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	private JSONRacer mProfile = RaceTrackerApp.mRacer;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_profile);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
            	findViewById(R.id.profile_edit).setVisibility(View.GONE);
                String value = extras.getString("profile");
                System.out.println(value);
               try {
				mProfile = new JSONRacer(new JSONObject(value));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            }
            
            //mProfile = RaceTrackerApp.testRacers.get(0);
            if(mProfile.getAvatar() > 0){
            ((ImageView) findViewById(R.id.profile_avatar)).setImageDrawable(getResources().getDrawable(mProfile.getAvatar()));}
            ((TextView) findViewById(R.id.profile_name)).setText(mProfile.getFirstName() + " " + mProfile.getLastName());
            ((TextView) findViewById(R.id.profile_title)).setText(mProfile.getTitle());
            ((TextView) findViewById(R.id.profile_total_races)).setText(mProfile.getTotalRaces()+"");
            ((TextView) findViewById(R.id.profile_distance)).setText(mProfile.getTotalDistance()+"Km");
            ((TextView) findViewById(R.id.profile_won)).setText(mProfile.getWins()+"");
            ((TextView) findViewById(R.id.profile_lost)).setText(mProfile.getWins()+"");
            ((TextView) findViewById(R.id.profile_speed)).setText(mProfile.getCurrentSpeed()+"Km/h");
            
            /*
            final AvatarSurfaceView asv = (AvatarSurfaceView) this.findViewById(R.id.profile_surfaceview);
            ((Button) this.findViewById(R.id.profile_pic_button)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if(toggle){
					asv.startCamera();
					toggle = false;
					}
					else{
						asv.stopCamera();
						toggle = true;
					}
					
				}
            	
            });
            ((Button)this.findViewById(R.id.profile_name_button)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);

					alert.setTitle("Enter name");
					alert.setMessage("");

					// Set an EditText view to get user input 
					final EditText input = new EditText(ProfileActivity.this);
					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					  String value = input.getText().toString();
					  ((TextView)ProfileActivity.this.findViewById(R.id.profile_name_text)).setText(value);
					  }
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					  }
					});

					alert.show();
				}
            });*/
            //((TextView)this.findViewById(R.id.profile_distance)).setText(""+(Math.round(RaceTrackerApp.mProfile.kilometersTraveled * 100.0)*10));
            //((TextView)this.findViewById(R.id.profile_speed)).setText(""+RaceTrackerApp.mProfile.avgSpeed);
            
            ((Button) this.findViewById(R.id.profile_edit)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(getBaseContext(), SettingsActivity.class));
				}
            	
            });
            
            
        }
    }