package edu.oakland.racetracker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView mTextOutput;
	private EditText mUrlInput;
	private Button mGetButton;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTextOutput = (TextView) findViewById(R.id.text);
		mUrlInput = (EditText) findViewById(R.id.edit);
		mGetButton = (Button) findViewById(R.id.button);
		
		mUrlInput.setText("http://www.google.com");
		
		mGetButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String url = mUrlInput.getText().toString();
				String path = getCacheDir() + "/test.log";
				new AsyncTask<String, Void, File>(){
					@Override
					protected File doInBackground(String... args) {
						return downloadFile(args[0], args[1]);
					}
				    @Override
				    protected void onPostExecute(File file){
				    	if(file != null){
				    		StringBuilder text = new StringBuilder();
				    		try {
				    		    BufferedReader br = new BufferedReader(new FileReader(file));
				    		    String line;
				    		    while ((line = br.readLine()) != null) {
				    		        text.append(line);
				    		        text.append('\n');
				    		    }
				    		}
				    		catch (IOException e) {Log.e("MainActivity", e.getMessage());}
				    		mTextOutput.setText(text.toString());
				    	}
				    }
				}.execute(url, path);
			}
		});
	}
	
	private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected()/* && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI*/);
    }
	
	
	private File downloadFile(String address, String filePath){
		File file = null;
		 try {
			 if(!isNetworkAvailable()){
				 throw new IOException("No internet connection");
			}
			URL url = new URL(address);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			
			final int BUFFER_SIZE = 64 * 1024;
			//double length = urlConnection.getContentLength();
			//double have = 0;
			
			InputStream is1 = url.openStream();
			BufferedInputStream bis = new BufferedInputStream(is1, BUFFER_SIZE);

			file = new File(filePath);
			if(file.exists() && file.isFile()){ //overwrite
				file.delete();
			}
			file.createNewFile();
			file.setReadable(true, false);
			file.setWritable(true, false);
			file.setExecutable(true, false);
			
			FileOutputStream fos = new FileOutputStream(file);
			byte[] baf = new byte[BUFFER_SIZE];
			int chunk = 0;
			while ((chunk = bis.read(baf, 0, BUFFER_SIZE)) > -1) {
				//have += chunk;
				fos.write(baf, 0, chunk);
			}
			fos.flush();
			fos.close();
			is1.close();
			
			file.setReadable(true, false);
			file.setWritable(true, false);
			file.setExecutable(true, false);
		} catch (IOException e) {Log.e("MainActivity", e.getMessage());}
		return file;
	}
}
