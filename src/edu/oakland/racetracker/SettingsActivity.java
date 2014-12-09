package edu.oakland.racetracker;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
/**
 * Settings activity for the profile editor
 * @author Lukas Greib
 *
 */
public class SettingsActivity extends Activity{
	private static Context mContext;

	public static class PrefsFragment extends PreferenceFragment  implements OnSharedPreferenceChangeListener{
		private void sync(){
			SharedPreferences sp = getPreferenceManager().getSharedPreferences();
			ParseRacer r = RaceTrackerApp.mRacer;
			r.firstName = sp.getString("firstName", "");
			r.lastName = sp.getString("lastName", "");
			r.title = sp.getString("title", "");
			r.saveInBackground(new SaveCallback(){
				@Override
				public void done(ParseException arg0) {
					if(arg0 != null){
						Toast.makeText(mContext, arg0.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			getPreferenceScreen().findPreference("avatarCamera").setOnPreferenceClickListener(new OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference arg0) {
					Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(takePicture, 0);
					return false;
				}
			});
			getPreferenceScreen().findPreference("avatarFile").setOnPreferenceClickListener(new OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference arg0) {
					Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(pickPhoto , 1);
					return false;
				}
			});
		}
		
		@Override
		public void onResume() {
		    super.onResume();
		    SharedPreferences sp = getPreferenceManager().getSharedPreferences();
		    Set<String> keys = sp.getAll().keySet();
		    for(String key : keys){
		    	this.onSharedPreferenceChanged(sp, key);
		    }
		    sp.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
		    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		    super.onPause();
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
			super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
			if(resultCode == RESULT_OK){
				Bitmap bmp = (Bitmap) imageReturnedIntent.getExtras().get("data");
				RaceTrackerApp.mRacer.setAvatarDrawable(new BitmapDrawable(mContext.getResources(), bmp));
				sync();
			}
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			Preference pref = this.findPreference(key);
			if (pref instanceof EditTextPreference) {
				pref.setSummary(((EditTextPreference) pref).getText());
		    }
			sync();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
	}
}