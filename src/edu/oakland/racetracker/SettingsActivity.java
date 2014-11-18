package edu.oakland.racetracker;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity{
	private static Context mContext;

	public static class PrefsFragment extends PreferenceFragment  implements OnSharedPreferenceChangeListener{
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
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
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			Preference pref = this.findPreference(key);
			if (pref instanceof EditTextPreference) {
				pref.setSummary(((EditTextPreference) pref).getText());
		    }
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
	}
}