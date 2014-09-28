package edu.oakland.racetracker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private NetworkHelper mNetworkHelper;
	private TextView mTextOutput;
	private EditText mURL, mUserID, mFirstName, mLastName, mTimeStamp;
	private Button mGetButton;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNetworkHelper = new NetworkHelper(this);
		
		//Get references to GUI elements
		mTextOutput = (TextView) findViewById(R.id.text);
		mURL = (EditText) findViewById(R.id.URL);
		mUserID = (EditText) findViewById(R.id.UserID);
		mFirstName = (EditText) findViewById(R.id.FirstName);
		mLastName = (EditText) findViewById(R.id.LastName);
		mTimeStamp = (EditText) findViewById(R.id.TimeStamp);
		mGetButton = (Button) findViewById(R.id.button);
		
		mGetButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				final String url = mURL.getText().toString();
				final String userId = mUserID.getText().toString();
				final String firstName = mFirstName.getText().toString();
				final String lastName = mLastName.getText().toString();
				final String timeStamp = mTimeStamp.getText().toString();
				new AsyncTask<Void, Void, String>(){
					@Override
					protected String doInBackground(Void... args) {
						String urlParameters = "";
						try {
							urlParameters = "serial=" + URLEncoder.encode(android.os.Build.SERIAL, "UTF-8") +
							"&UserID=" + URLEncoder.encode(userId, "UTF-8") +
							"&FirstName=" + URLEncoder.encode(firstName, "UTF-8") +
							"&LastName=" + URLEncoder.encode(lastName, "UTF-8") +
							"&TimeStamp=" + URLEncoder.encode(timeStamp, "UTF-8") +
							"&model=" + URLEncoder.encode(android.os.Build.MODEL, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						return mNetworkHelper.post(url, urlParameters);
					}
				    @Override
				    protected void onPostExecute(String responseText){
				    	Log.d("POST", responseText);
				    }
				}.execute();
			}
		});
	}
}
