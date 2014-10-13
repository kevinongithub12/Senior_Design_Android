package edu.oakland.racetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	private boolean toggle = true;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_profile);
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
            });
            ((TextView)this.findViewById(R.id.profile_travelled_text)).setText(""+(Math.round(RaceTrackerApp.mProfile.kilometersTraveled * 100.0)*10));
            ((TextView)this.findViewById(R.id.profile_speed_text)).setText(""+RaceTrackerApp.mProfile.avgSpeed);*/
            
            ((Button) this.findViewById(R.id.profile_edit)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(getBaseContext(), SettingsActivity.class));
				}
            	
            });
            
            
        }
    }