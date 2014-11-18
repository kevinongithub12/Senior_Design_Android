package edu.oakland.racetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	    private ParseRacer racer;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            racer = RaceTrackerApp.viewingProfile;
            this.setContentView(R.layout.activity_profile);

            ((ImageView) findViewById(R.id.profile_avatar)).setImageDrawable(racer.getAvatarDrawable());
            ((TextView) findViewById(R.id.profile_name)).setText(racer.firstName + " " + racer.lastName);
            ((TextView) findViewById(R.id.profile_title)).setText(racer.title);
            ((TextView) findViewById(R.id.profile_total_races)).setText(racer.totalRaces+"");
            ((TextView) findViewById(R.id.profile_won)).setText(racer.wins+"");
            ((TextView) findViewById(R.id.profile_lost)).setText(racer.losses+"");
            //((TextView) findViewById(R.id.profile_speed)).setText(racer.+" Km/h");
            
            ((Button) this.findViewById(R.id.profile_edit)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(getBaseContext(), SettingsActivity.class));
				}
            	
            });
        }
    }