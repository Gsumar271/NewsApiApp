package com.newsapiapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created on 1/11/18.
 */

public class SettingsActivity extends Activity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREF_AUTO_UPDATE = "checkbox_preference";
    public static final String PREF_CATEGORY = "category_preference";
    public static final String PREF_UPDATE_FREQ = "update_freq_preference";


    SharedPreferences prefs;
    boolean autoUpdate;
    String cat;
    int category;
    int freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PreferencesFragment())
                .commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        prefs = sharedPreferences;


        if (key.equals(PREF_AUTO_UPDATE)) {

            autoUpdate = sharedPreferences.getBoolean(key, false);


        } else if (key.equals(PREF_CATEGORY)) {


            category = sharedPreferences.getInt(key, -1);
            switch (category) {
                case 1:
                    cat = "US";
                    break;
                case 2:
                    cat = "WORLD";
                    break;
                case 3:
                    cat = "SPORTS";
                    break;
                case 4:
                    cat = "TRAVEL";
                    break;
                case 5:
                    cat = "BUSINESS";
                    break;
                default:
                    cat = "WORLD";
                    break;

            }
        } else if (key.equals(PREF_UPDATE_FREQ)) {

            freq = sharedPreferences.getInt(key, -1);

        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_AUTO_UPDATE, autoUpdate);
        editor.putInt(PREF_CATEGORY, category);
        editor.putInt(PREF_UPDATE_FREQ, freq);
        editor.commit();

    }
}

