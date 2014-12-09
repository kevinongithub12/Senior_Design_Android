package edu.oakland.racetracker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 
 * @author Lukas Greib
 * Helper functions for network connections
 */
public class NetworkHelper{
	
	private Context mContext;
	
	public NetworkHelper(Context context){
		mContext = context;
	}
	
	public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected()/* && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI*/);
    }
	
	public static interface NetworkCallback{
		public void onProgress(int percent);
		public void onSuccess(File file);
		public void onError(String message);
	}
	
	public int ping(String address){
		int responseCode = -1;
		try {
		    URL url = new URL(address);
		    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		    urlc.setRequestMethod("HEAD");
		    urlc.setRequestProperty("Connection", "close");
		    urlc.setConnectTimeout(1000 * 10);
		    urlc.connect();
		    responseCode = urlc.getResponseCode();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return responseCode;
	}
	
	public String get(String address){
		String ret = "";
		try{
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		 BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      ret = response.toString();
	    } catch (Exception e) {e.printStackTrace();} 
		return ret;
	}

	public File download(String address, String filePath, NetworkCallback downloadCallback){
		File file = null;
		 try {
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			
			final int BUFFER_SIZE = 64 * 1024;
			final double length = connection.getContentLength();
			double have = 0;
			int percent = 0;
			
			InputStream is1 = url.openStream();
			BufferedInputStream bis = new BufferedInputStream(is1, BUFFER_SIZE);

			file = new File(filePath);
			if(file.exists() && file.isFile()){
				file.delete();
			}
			file.createNewFile();
			file.setReadable(false, false);
			file.setWritable(true, false);
			file.setExecutable(false, false);
			
			FileOutputStream fos = new FileOutputStream(file);
			byte[] baf = new byte[BUFFER_SIZE];
			int chunk = 0;
			while ((chunk = bis.read(baf, 0, BUFFER_SIZE)) > -1) {
				fos.write(baf, 0, chunk);
				have += chunk;
				int temp = (int)((have/length)*100);
				if(downloadCallback != null && temp > percent){
				    percent = temp;
				    downloadCallback.onProgress(percent);
				}
			}
			fos.flush();
			fos.close();
			is1.close();
			
			file.setReadable(true, false);
			file.setWritable(true, false);
			file.setExecutable(true, false);
			
			if(downloadCallback != null){
				downloadCallback.onSuccess(file);
			}
			return file;

		} catch (IOException e) {
			if(downloadCallback != null){
				downloadCallback.onError(e.getMessage());
			}
		}
		return file;
	}
	
	public String post(String baseAddress, String urlParameters){
		    String ret = "";
			try {
		      URL url = new URL(baseAddress);
		      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		      connection.setRequestMethod("POST");
		      connection.setRequestProperty("Connection", "close");
		      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			  connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		      connection.setRequestProperty("Content-Language", "en-US");  			
		      connection.setUseCaches (false);
		      connection.setDoInput(true);
		      //connection.setDoOutput(true);

		      DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		      wr.writeBytes (urlParameters);
		      wr.flush();
		      wr.close();

		      BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		      String line;
		      StringBuffer response = new StringBuffer(); 
		      while((line = rd.readLine()) != null) {
		        response.append(line);
		        response.append('\r');
		      }
		      rd.close();
		      connection.disconnect();
		      ret = response.toString();
		    } catch (Exception e) {e.printStackTrace();}
			return ret;
	}
}