package edu.oakland.racetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button) findViewById(R.id.main_profile_button)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, ProfileActivity.class));
			}
		});
		((Button) findViewById(R.id.main_map_test)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, MyMapActivity.class));
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
