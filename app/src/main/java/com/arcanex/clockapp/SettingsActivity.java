package com.arcanex.clockapp;


import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toolbar;

import static com.arcanex.clockapp.MainActivity.internet_autoconnection;
import static com.arcanex.clockapp.MainActivity.internet_autoconnection_time_in_sec;
import static com.arcanex.clockapp.MainActivity.internet_password;
import static com.arcanex.clockapp.MainActivity.internet_port;
import static com.arcanex.clockapp.MainActivity.internet_server;
import static com.arcanex.clockapp.MainActivity.internet_user;
import static com.arcanex.clockapp.MainActivity.prefs;
import static com.arcanex.clockapp.MainActivity.ssid;
import static com.arcanex.clockapp.MainActivity.wifi_password;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.application_settings);

        findPreference("server").setSummary(prefs.getString("server", "none"));
        findPreference("port").setSummary(prefs.getString("port", "none"));
        findPreference("user").setSummary(prefs.getString("user", "none"));
        findPreference("mqtt_password").setSummary(prefs.getString("password", "none"));
        findPreference("autoConnect_timer").setSummary("Текущее значение - " + prefs.getString("autoConnect_timer", "10") + " секунд");
        findPreference("ssid").setSummary(prefs.getString("ssid", "none"));
        findPreference("wifi_password").setSummary(prefs.getString("wifi_password", "none"));



        findPreference("port").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                internet_port = o.toString();
                return true;
            }
        });
        findPreference("server").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                internet_server = o.toString();
                return true;
            }
        });
        findPreference("user").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                internet_user = o.toString();
                return true;
            }
        });
        findPreference("mqtt_password").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString());
                internet_password = o.toString();
                return true;
            }
        });
        findPreference("autoConnect_switch").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                internet_autoconnection = Boolean.getBoolean(o.toString());
                return true;
            }
        });
        findPreference("autoConnect_timer").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                internet_autoconnection_time_in_sec = Integer.parseInt(o.toString());
                preference.setSummary("Текущее значение - " + o.toString() + " секунд");
                return true;
            }
        });
        findPreference("ssid").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ssid = o.toString();
                preference.setSummary(o.toString());
                return true;
            }
        });
        findPreference("wifi_password").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                wifi_password = o.toString();
                preference.setSummary(o.toString());
                return true;
            }
        });






        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {


            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);


    }


}
